package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.DashboardClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.DashboardConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/web-ui")
@Api(value = "/web-ui", tags = "WebUI接口")
public class WebUIController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#config
     */
    @GetMapping("config")
    @ApiOperation("WebUI 配置")
    public CompletableFuture<DashboardConfiguration> config() throws IOException {
        DashboardClient dashboardClient = restClient.dashboard();
        return dashboardClient.config();
    }
}
