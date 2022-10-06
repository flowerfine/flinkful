package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flink/taskmanagers")
@Api(value = "/taskmanagers", tags = "TaskManager接口")
public class TaskManagerController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers
     */
    @GetMapping
    @ApiOperation("taskmanagers 概览")
    public CompletableFuture<TaskManagersInfo> taskManagers() throws IOException {
        return restClient.taskManager().taskManagers();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers-metrics
     */
    @GetMapping("metrics")
    @ApiOperation("taskmanagers metrics")
    public CompletableFuture<AggregatedMetricsResponseBody> taskMangersMetrics(@RequestParam(value = "get", required = false) Optional<String> get,
                                                                               @RequestParam(value = "agg", required = false) Optional<String> agg,
                                                                               @RequestParam(value = "taskmanagers", required = false) Optional<String> taskmanagers) throws IOException {
        return restClient.taskManager().taskManagersMetrics(get, agg, taskmanagers);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers-taskmanagerid
     */
    @GetMapping("{taskManagerId}")
    @ApiOperation("TaskManager 详情")
    public CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerDetail(taskManagerId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers-taskmanagerid-metrics
     */
    @GetMapping("{taskManagerId}/metrics")
    @ApiOperation("taskmanagers metrics")
    public CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(@PathVariable("taskManagerId") String taskManagerId,
                                                                              @RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.taskManager().taskManagerMetrics(taskManagerId, get);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers-taskmanagerid-logs
     */
    @GetMapping("{taskManagerId}/logs")
    @ApiOperation("TaskManager 日志文件")
    public CompletableFuture<LogListInfo> taskManagerLogs(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerLogs(taskManagerId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#taskmanagers-taskmanagerid-thread-dump
     */
    @GetMapping("{taskManagerId}/thread-dump")
    @ApiOperation("dump TaskManager thread")
    public CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerThreadDump(taskManagerId);
    }
}
