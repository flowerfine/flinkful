package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.ConfigurationInfo;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/job-manager")
@Api(value = "/job-manager", tags = "JobManager接口")
public class JobManagerController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jobmanager-config
     */
    @GetMapping("config")
    @ApiOperation("JobManager 配置")
    public CompletableFuture<ConfigurationInfo> config() throws IOException {
        return restClient.jobManager().jobmanagerConfig();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jobmanager-logs
     */
    @GetMapping("logs")
    @ApiOperation("JobManager 日志文件")
    public CompletableFuture<LogListInfo> logs() throws IOException {
        return restClient.jobManager().jobmanagerLogs();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jobmanager-metrics
     */
    @GetMapping("metrics")
    @ApiOperation("JobManager metrics")
    public CompletableFuture<MetricCollectionResponseBody> metrics(@RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.jobManager().jobmanagerMetrics(get);
    }
}
