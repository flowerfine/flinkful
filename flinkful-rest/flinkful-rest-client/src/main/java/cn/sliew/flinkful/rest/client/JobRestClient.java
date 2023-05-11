package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.v1.client.JobClient;
import org.apache.flink.runtime.messages.webmonitor.JobIdsWithStatusOverview;
import org.apache.flink.runtime.messages.webmonitor.MultipleJobsDetails;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.handler.job.rescaling.RescalingStatusHeaders;
import org.apache.flink.runtime.rest.handler.job.rescaling.RescalingStatusMessageParameters;
import org.apache.flink.runtime.rest.handler.job.rescaling.RescalingTriggerHeaders;
import org.apache.flink.runtime.rest.handler.job.rescaling.RescalingTriggerMessageParameters;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.checkpoints.*;
import org.apache.flink.runtime.rest.messages.job.*;
import org.apache.flink.runtime.rest.messages.job.metrics.*;
import org.apache.flink.runtime.rest.messages.job.savepoints.*;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointTriggerHeaders;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class JobRestClient implements JobClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public JobRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<MultipleJobsDetails> jobsOverview() throws IOException {
        return client.sendRequest(address, port, JobsOverviewHeaders.getInstance());
    }

    @Override
    public CompletableFuture<AggregatedMetricsResponseBody> jobsMetric(Optional<String> get, Optional<String> agg, Optional<String> jobs) throws IOException {
        AggregatedJobMetricsParameters parameters = new AggregatedJobMetricsParameters();
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metrics.resolveFromString(metrics)));
        agg.ifPresent(aggs -> toIllegalArgument(() -> parameters.aggs.resolveFromString(aggs)));
        jobs.ifPresent(job -> toIllegalArgument(() -> parameters.selector.resolveFromString(job)));
        return client.sendRequest(address, port, AggregatedJobMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobIdsWithStatusOverview> jobs() throws IOException {
        return client.sendRequest(address, port, JobIdsWithStatusesOverviewHeaders.getInstance());
    }

    @Override
    public CompletableFuture<JobDetailsInfo> jobDetail(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, JobDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobSubmitResponseBody> jobSubmit(JobSubmitRequestBody requestBody) throws IOException {
        return client.sendRequest(address, port, JobSubmitHeaders.getInstance(), EmptyMessageParameters.getInstance(), requestBody);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> jobTerminate(String jobId, String mode) throws IOException {
        JobCancellationMessageParameters parameters = new JobCancellationMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.terminationModeQueryParameter.resolveFromString(mode));
        return client.sendRequest(address, port, JobCancellationHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobAccumulatorsInfo> jobAccumulators(String jobId, Optional<Boolean> includeSerializedValue) throws IOException {
        JobAccumulatorsMessageParameters parameters = new JobAccumulatorsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        includeSerializedValue.ifPresent(param -> toIllegalArgument(() -> parameters.includeSerializedAccumulatorsParameter.resolveFromString(param.toString())));
        return client.sendRequest(address, port, JobAccumulatorsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<CheckpointingStatistics> jobCheckpoints(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, CheckpointingStatisticsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<CheckpointConfigInfo> jobCheckpointConfig(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, CheckpointConfigHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<CheckpointStatistics> jobCheckpointDetail(String jobId, Long checkpointId) throws IOException {
        CheckpointMessageParameters parameters = new CheckpointMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.checkpointIdPathParameter.resolveFromString(checkpointId.toString()));
        return client.sendRequest(address, port, CheckpointStatisticDetailsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<TaskCheckpointStatisticsWithSubtaskDetails> jobCheckpointSubtaskDetail(String jobId, Long checkpointId, String vertexId) throws IOException {
        TaskCheckpointMessageParameters parameters = new TaskCheckpointMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.checkpointIdPathParameter.resolveFromString(checkpointId.toString()));
        toIllegalArgument(() -> parameters.jobVertexIdPathParameter.resolveFromString(vertexId));
        return client.sendRequest(address, port, TaskCheckpointStatisticsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobConfigInfo> jobConfig(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, JobConfigHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobExceptionsInfoWithHistory> jobException(String jobId, Optional<String> maxExceptions) throws IOException {
        JobExceptionsMessageParameters parameters = new JobExceptionsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        maxExceptions.ifPresent(param -> toIllegalArgument(() -> parameters.upperLimitExceptionParameter.resolveFromString(param)));
        return client.sendRequest(address, port, JobExceptionsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobExecutionResultResponseBody> jobExecutionResult(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, JobExecutionResultHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobMetrics(String jobId, Optional<String> get) throws IOException {
        JobMetricsMessageParameters parameters = new JobMetricsMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        return client.sendRequest(address, port, JobMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobPlanInfo> jobPlan(String jobId) throws IOException {
        JobMessageParameters parameters = new JobMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        return client.sendRequest(address, port, JobPlanHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<TriggerResponse> jobRescale(String jobId, Integer parallelism) throws IOException {
        RescalingTriggerMessageParameters parameters = new RescalingTriggerMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.rescalingParallelismQueryParameter.resolveFromString(parallelism.toString()));
        return client.sendRequest(address, port, RescalingTriggerHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> jobRescaleResult(String jobId, String triggerId) throws IOException {
        RescalingStatusMessageParameters parameters = new RescalingStatusMessageParameters();
        toIllegalArgument(() -> parameters.jobPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.triggerIdPathParameter.resolveFromString(triggerId));
        return client.sendRequest(address, port, RescalingStatusHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<TriggerResponse> jobSavepoint(String jobId, SavepointTriggerRequestBody requestBody) throws IOException {
        SavepointTriggerMessageParameters parameters = new SavepointTriggerMessageParameters();
        toIllegalArgument(() -> parameters.jobID.resolveFromString(jobId));
        return client.sendRequest(address, port, SavepointTriggerHeaders.getInstance(), parameters, requestBody);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<SavepointInfo>> jobSavepointResult(String jobId, String triggerId) throws IOException {
        SavepointStatusMessageParameters parameters = new SavepointStatusMessageParameters();
        toIllegalArgument(() -> parameters.jobIdPathParameter.resolveFromString(jobId));
        toIllegalArgument(() -> parameters.triggerIdPathParameter.resolveFromString(triggerId));
        return client.sendRequest(address, port, SavepointStatusHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<TriggerResponse> jobStop(String jobId, StopWithSavepointRequestBody requestBody) throws IOException {
        SavepointTriggerMessageParameters parameters = new SavepointTriggerMessageParameters();
        toIllegalArgument(() -> parameters.jobID.resolveFromString(jobId));
        return client.sendRequest(address, port, StopWithSavepointTriggerHeaders.getInstance(), parameters, requestBody);
    }
}
