package cn.sliew.flinkful.sql.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.endpoint.SqlGatewayEndpoint;
import org.apache.flink.table.gateway.api.endpoint.SqlGatewayEndpointFactoryUtils;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.service.SqlGatewayServiceImpl;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SqlGatewayConfig implements InitializingBean, DisposableBean {

    private SessionManager sessionManager;
    private List<SqlGatewayEndpoint> endpoints = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultContext context =
                DefaultContext.load(new Configuration());
        sessionManager = new SessionManager(context);

        sessionManager.start();
        SqlGatewayService sqlGatewayService = new SqlGatewayServiceImpl(sessionManager);
        try {
            endpoints.addAll(
                    SqlGatewayEndpointFactoryUtils.createSqlGatewayEndpoint(
                            sqlGatewayService, context.getFlinkConfig()));
            for (SqlGatewayEndpoint endpoint : endpoints) {
                endpoint.start();
            }
        } catch (Throwable t) {
            log.error("Failed to start the endpoints.", t);
            throw new SqlGatewayException("Failed to start the endpoints.", t);
        }
    }

    @Override
    public void destroy() throws Exception {
        for (SqlGatewayEndpoint endpoint : endpoints) {
            stopEndpointSilently(endpoint);
        }
        if (sessionManager != null) {
            sessionManager.stop();
        }
    }

    private void stopEndpointSilently(SqlGatewayEndpoint endpoint) {
        try {
            endpoint.stop();
        } catch (Exception e) {
            log.error("Failed to stop the endpoint. Ignore.", e);
        }
    }

}
