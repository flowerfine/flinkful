package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.TaskManagerClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.*;
import org.apache.flink.runtime.rest.messages.taskmanager.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class TaskManagerRestClient implements TaskManagerClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public TaskManagerRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<TaskManagersInfo> taskManagers() throws IOException {
        return client.sendRequest(address, port, TaskManagersHeaders.getInstance());
    }

    @Override
    public CompletableFuture<AggregatedMetricsResponseBody> taskManagersMetrics(Optional<String> get, Optional<String> agg, Optional<String> taskmanagers) throws IOException {
        AggregateTaskManagerMetricsParameters parameters = new AggregateTaskManagerMetricsParameters();
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metrics.resolveFromString(metrics)));
        agg.ifPresent(aggs -> toIllegalArgument(() -> parameters.aggs.resolveFromString(aggs)));
        taskmanagers.ifPresent(taskmanager -> toIllegalArgument(() -> parameters.selector.resolveFromString(taskmanager)));
        return client.sendRequest(address, port, AggregatedTaskManagerMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(String taskManagerId) throws IOException {
        TaskManagerMessageParameters parameters = new TaskManagerMessageParameters();
        toIllegalArgument(() -> parameters.taskManagerIdParameter.resolveFromString(taskManagerId));
        return client.sendRequest(address, port, TaskManagerDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<LogListInfo> taskManagerLogs(String taskManagerId) throws IOException {
        TaskManagerMessageParameters parameters = new TaskManagerMessageParameters();
        toIllegalArgument(() -> parameters.taskManagerIdParameter.resolveFromString(taskManagerId));
        return client.sendRequest(address, port, TaskManagerLogsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(String taskManagerId, Optional<String> get) throws IOException {
        TaskManagerMetricsMessageParameters parameters = new TaskManagerMetricsMessageParameters();
        toIllegalArgument(() -> parameters.taskManagerIdParameter.resolveFromString(taskManagerId));
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        return client.sendRequest(address, port, TaskManagerMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(String taskManagerId) throws IOException {
        TaskManagerMessageParameters parameters = new TaskManagerMessageParameters();
        toIllegalArgument(() -> parameters.taskManagerIdParameter.resolveFromString(taskManagerId));
        return client.sendRequest(address, port, TaskManagerThreadDumpHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }
}
