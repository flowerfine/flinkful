package cn.sliew.flinkful.sql.gateway.embedded.controller;

import cn.sliew.flinkful.sql.gateway.embedded.service.FlinkfulSqlGatewayService;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.CatalogInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.FunctionInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.TableInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/flinkful/gateway")
@Tag(name = "Sql Gateway接口")
public class FlinkfulSqlGatewayController {

    @Autowired
    private FlinkfulSqlGatewayService sqlGatewayService;

    @GetMapping("getCatalogInfo")
    @Operation(summary = "查询 catalogs 全部信息", description = "查询 catalogs 全部信息")
    public Set<CatalogInfo> getCatalogInfo() {
        return sqlGatewayService.getCatalogInfo();
    }

    @GetMapping("catalogs")
    @Operation(summary = "查询 catalogs", description = "查询 catalogs")
    public List<String> listCatalogs() {
        return sqlGatewayService.listCatalogs();
    }

    @GetMapping("catalogs/{catalogName}/databases")
    @Operation(summary = "查询 databases", description = "查询 databases")
    public List<String> listDatabases(@PathVariable("catalogName") String catalogName) {
        return sqlGatewayService.listDatabases(catalogName);
    }

    @GetMapping("catalogs/{catalogName}/databases/{databaseName}/tables")
    @Operation(summary = "查询 tables", description = "查询 tables")
    public List<TableInfo> listTables(@PathVariable("catalogName") String catalogName, @PathVariable("databaseName") String databaseName) {
        return sqlGatewayService.listTables(catalogName, databaseName);
    }

    @GetMapping("catalogs/{catalogName}/databases/{databaseName}/views")
    @Operation(summary = "查询 views", description = "查询 views")
    public List<TableInfo> listViews(@PathVariable("catalogName") String catalogName, @PathVariable("databaseName") String databaseName) {
        return sqlGatewayService.listViews(catalogName, databaseName);
    }

    @GetMapping("catalogs/{catalogName}/databases/{databaseName}/udfs")
    @Operation(summary = "查询 udfs", description = "查询 udfs")
    public List<FunctionInfo> listUdfs(@PathVariable("catalogName") String catalogName, @PathVariable("databaseName") String databaseName) {
        return sqlGatewayService.listUdfs(catalogName, databaseName);
    }

}
