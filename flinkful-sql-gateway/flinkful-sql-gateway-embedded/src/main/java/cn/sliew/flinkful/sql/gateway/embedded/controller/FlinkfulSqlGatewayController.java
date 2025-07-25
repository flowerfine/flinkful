package cn.sliew.flinkful.sql.gateway.embedded.controller;

import cn.sliew.flinkful.sql.gateway.embedded.service.FlinkfulSqlGatewayService;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.CatalogInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/flinkful/gateway")
@Tag(name = "Sql Gateway接口")
public class FlinkfulSqlGatewayController {

    @Autowired
    private FlinkfulSqlGatewayService sqlGatewayService;

    @GetMapping("getCatalogInfo")
    @Operation(summary = "查询 catalogs", description = "查询 catalogs")
    public Set<CatalogInfo> getCatalogInfo() {
        return sqlGatewayService.getCatalogInfo();
    }
}
