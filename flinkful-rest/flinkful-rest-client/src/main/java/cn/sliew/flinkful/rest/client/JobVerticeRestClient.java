package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.JobVerticeClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.RestClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.*;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.*;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.metrics.*;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.webmonitor.threadinfo.JobVertexFlameGraph;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class JobVerticeRestClient implements JobVerticeClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public JobVerticeRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<JobVertexDetailsInfo> jobVertexDetail(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, JobVertexDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobVertexAccumulatorsInfo> jobVertexAccumulators(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, JobVertexAccumulatorsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobVertexBackPressureInfo> jobVertexBackPressure(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, JobVertexBackPressureHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobVertexFlameGraph> jobVertexFlameGraph(String jobId, String vertexId, Optional<String> type) throws IOException {
        JobVertexFlameGraphParameters parameters = new JobVertexFlameGraphParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        type.ifPresent(typeStr -> toIllegalArgument(() -> parameters.flameGraphTypeQueryParameter.resolveFromString(typeStr)));
        return client.sendRequest(address, port, JobVertexFlameGraphHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexMetrics(String jobId, String vertexId, Optional<String> get) throws IOException {
        JobVertexMetricsMessageParameters parameters = new JobVertexMetricsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        return client.sendRequest(address, port, JobVertexMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<SubtasksTimesInfo> jobVertexSubtaskTimes(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, SubtasksTimesHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobVertexTaskManagersInfo> jobVertexTaskManagers(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, JobVertexTaskManagersHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexWatermarks(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, JobVertexWatermarksHeaders.INSTANCE, parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<SubtasksAllAccumulatorsInfo> jobVertexSubtaskAccumulators(String jobId, String vertexId) throws IOException {
        JobVertexMessageParameters parameters = new JobVertexMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, SubtasksAllAccumulatorsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    /**
     * fixme wrong headers
     */
    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Optional<String> get, Optional<String> agg, Optional<String> subtasks) throws IOException {
        SubtaskMetricsMessageParameters parameters = new SubtaskMetricsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        subtasks.ifPresent(subtask -> toIllegalArgument(() -> parameters.subtaskIndexPathParameter.resolveFromString(subtask)));
        return client.sendRequest(address, port, SubtaskMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskDetail(String jobId, String vertexId, Integer subtaskindex) throws IOException {
        SubtaskMessageParameters parameters = new SubtaskMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        toIllegalArgument(() -> parameters.subtaskIndexPathParameter.resolveFromString(subtaskindex.toString()));
        return client.sendRequest(address, port, SubtaskCurrentAttemptDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Integer subtaskindex, String get) throws IOException {
        SubtaskMetricsMessageParameters parameters = new SubtaskMetricsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        toIllegalArgument(() -> parameters.subtaskIndexPathParameter.resolveFromString(subtaskindex.toString()));
        toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(get));
        return client.sendRequest(address, port, SubtaskMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskAttemptDetail(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException {
        SubtaskAttemptMessageParameters parameters = new SubtaskAttemptMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        toIllegalArgument(() -> parameters.subtaskIndexPathParameter.resolveFromString(subtaskindex.toString()));
        toIllegalArgument(() -> parameters.subtaskAttemptPathParameter.resolveFromString(attempt.toString()));
        return client.sendRequest(address, port, SubtaskExecutionAttemptDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptAccumulatorsInfo> jobVertexSubtaskAttemptAccumulators(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException {
        SubtaskAttemptMessageParameters parameters = new SubtaskAttemptMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        toIllegalArgument(() -> parameters.subtaskIndexPathParameter.resolveFromString(subtaskindex.toString()));
        toIllegalArgument(() -> parameters.subtaskAttemptPathParameter.resolveFromString(attempt.toString()));
        return client.sendRequest(address, port, SubtaskExecutionAttemptAccumulatorsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

}
