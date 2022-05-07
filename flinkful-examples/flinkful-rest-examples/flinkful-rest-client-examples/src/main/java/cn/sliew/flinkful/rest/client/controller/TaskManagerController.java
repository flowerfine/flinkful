package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.ThreadDumpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flink/task-manager")
@Api(value = "/task-manager", tags = "TaskManager接口")
public class TaskManagerController {

    @Autowired
    private RestClient restClient;

    @GetMapping("/")
    public CompletableFuture<TaskManagersInfo> taskManagers() throws IOException {
        return restClient.taskManager().taskManagers();
    }

    @GetMapping("metrics")
    public CompletableFuture<AggregatedMetricsResponseBody> taskMangersMetrics(@RequestParam(value = "get", required = false) Optional<String> get,
                                                                               @RequestParam(value = "agg", required = false) Optional<String> agg,
                                                                               @RequestParam(value = "taskmanagers", required = false) Optional<String> taskmanagers) throws IOException {
        return restClient.taskManager().taskManagersMetrics(get, agg, taskmanagers);
    }

    @GetMapping("{taskManagerId}")
    public CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerDetail(taskManagerId);
    }

    @GetMapping("{taskManagerId}/metrics")
    public CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(@PathVariable("taskManagerId") String taskManagerId,
                                                                              @RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.taskManager().taskManagerMetrics(taskManagerId, get);
    }

    @GetMapping("{taskManagerId}/logs")
    public CompletableFuture<LogListInfo> taskManagerLogs(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerLogs(taskManagerId);
    }

    @GetMapping("{taskManagerId}/thread-dump")
    public CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerThreadDump(taskManagerId);
    }
}
