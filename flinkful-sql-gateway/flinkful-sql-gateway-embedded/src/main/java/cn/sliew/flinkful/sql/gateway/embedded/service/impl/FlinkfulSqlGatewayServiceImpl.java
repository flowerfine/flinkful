package cn.sliew.flinkful.sql.gateway.embedded.service.impl;

import cn.sliew.flinkful.sql.gateway.embedded.service.FlinkfulSqlGatewayService;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.CatalogInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.DatabaseInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.FunctionInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.TableInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.WsFlinkSqlGatewayQueryParam;
import org.apache.flink.configuration.*;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.SqlGatewayServiceImpl;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.apache.flink.table.gateway.service.session.SessionManagerImpl;
import org.apache.paimon.utils.ThreadUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FlinkfulSqlGatewayServiceImpl implements FlinkfulSqlGatewayService, InitializingBean, DisposableBean {

    private static final String SESSION_NAME = "test";

    private SessionManager sessionManager;
    private SqlGatewayService sqlGatewayService;
    private SessionHandle sessionHandle;

    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> future;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        configuration.set(JobManagerOptions.ADDRESS, "localhost");
        configuration.set(RestOptions.ADDRESS, "localhost");
        configuration.set(RestOptions.PORT, 8081);
        configuration.set(DeploymentOptions.TARGET, "remote");
        configuration.setString("table.catalog-store.kind", "gravitino");
        configuration.setString("table.catalog-store.gravitino.gravitino.metalake", "flink");
        configuration.setString("table.catalog-store.gravitino.gravitino.uri", "http://42.194.234.138:8090");
        configuration.setString("s3.endpoint", "http://localhost:9000");
        configuration.setString("s3.access-key", "admin");
        configuration.setString("s3.secret-key", "password");
        configuration.setString("s3.path.style.access", "true");

        DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
        sessionManager = new SessionManagerImpl(defaultContext);
        sessionManager.start();
        sqlGatewayService = new SqlGatewayServiceImpl(sessionManager);
        sessionHandle = sqlGatewayService.openSession(SessionEnvironment.newBuilder()
                        .registerCatalogCreator("paimon", (config, classloader) -> {
                            Map<String, String> options = Map.of(
                                    "type", "paimon",
                                    "warehouse", "s3://paimon/",
                                    "s3.endpoint", "http://localhost:9000",
                                    "s3.access-key", "admin",
                                    "s3.secret-key", "password",
                                    "s3.path.style.access", "true"
                            );
                            return FactoryUtil.createCatalog("paimon", options, configuration, classloader);
                        })
                .setSessionEndpointVersion(SqlGatewayRestAPIVersion.V2)
                .setSessionName(SESSION_NAME)
                .build());
        // 心跳
        executorService = Executors.newScheduledThreadPool(2);
        future = executorService.scheduleWithFixedDelay(() -> {
            sqlGatewayService.getSessionConfig(sessionHandle);
        }, 0,1, TimeUnit.MINUTES);
    }

    @Override
    public void destroy() throws Exception {
        sessionManager.stop();
        future.cancel(true);
        executorService.shutdown();
    }

    @Override
    public Set<CatalogInfo> getCatalogInfo() {
        Set<String> catalogs = sqlGatewayService.listCatalogs(sessionHandle);
        return catalogs.stream().map(catalogName -> {
            CatalogInfo catalogInfo = new CatalogInfo();
            catalogInfo.setCatalogName(catalogName);
            Set<DatabaseInfo> databases = sqlGatewayService.listDatabases(sessionHandle, catalogName)
                    .stream().map(databaseName -> {
                        DatabaseInfo databaseInfo = new DatabaseInfo();
                        databaseInfo.setDatabaseName(databaseName);
                        Set<TableInfo> tableInfos = sqlGatewayService.listTables(sessionHandle, catalogName, databaseName, Set.of(CatalogBaseTable.TableKind.TABLE))
                                .stream().map(table -> {
                                    TableInfo tableInfo = new TableInfo();
                                    tableInfo.setTableName(table.getIdentifier().getObjectName());
                                    tableInfo.setTableKind(table.getTableKind());
                                    return tableInfo;
                                }).collect(Collectors.toSet());
                        databaseInfo.setTables(tableInfos);
                        Set<TableInfo> viewInfos = sqlGatewayService.listTables(sessionHandle, catalogName, databaseName, Set.of(CatalogBaseTable.TableKind.VIEW))
                                .stream().map(table -> {
                                    TableInfo viewInfo = new TableInfo();
                                    viewInfo.setTableName(table.getIdentifier().getObjectName());
                                    viewInfo.setTableKind(table.getTableKind());
                                    return viewInfo;
                                }).collect(Collectors.toSet());
                        databaseInfo.setViews(viewInfos);
                        Set<FunctionInfo> functionInfos = sqlGatewayService.listUserDefinedFunctions(sessionHandle, catalogName, databaseName)
                                .stream().map(function -> {
                                    FunctionInfo functionInfo = new FunctionInfo();
                                    functionInfo.setFunctionName(function.getIdentifier().getFunctionName());
                                    functionInfo.setFunctionKind(function.getKind().orElse(null));
                                    return functionInfo;
                                }).collect(Collectors.toSet());
                        databaseInfo.setUserDefinedFunctions(functionInfos);
                        return databaseInfo;
                    }).collect(Collectors.toSet());
            catalogInfo.setDatabases(databases);
            return catalogInfo;
        }).collect(Collectors.toSet());
    }

    @Override
    public String executeSql(WsFlinkSqlGatewayQueryParam params) {
        return sqlGatewayService.executeStatement(sessionHandle, params.getSql(), Duration.ofMinutes(5L).toMillis(), new Configuration())
                .getIdentifier().toString();
    }

    @Override
    public ResultSet fetchResults(String operationHandleId, Long token, int maxRows) {
        OperationHandle operationHandle = new OperationHandle(UUID.fromString(operationHandleId));
        return sqlGatewayService.fetchResults(sessionHandle, operationHandle, token, maxRows);
    }

    @Override
    public Boolean cancel(String operationHandleId) {
        OperationHandle operationHandle = new OperationHandle(UUID.fromString(operationHandleId));
        sqlGatewayService.cancelOperation(sessionHandle, operationHandle);
        return true;
    }
}
