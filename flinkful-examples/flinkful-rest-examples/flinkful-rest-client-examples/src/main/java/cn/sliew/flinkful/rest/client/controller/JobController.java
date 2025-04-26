package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.runtime.messages.webmonitor.JobIdsWithStatusOverview;
import org.apache.flink.runtime.messages.webmonitor.MultipleJobsDetails;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.checkpoints.CheckpointConfigInfo;
import org.apache.flink.runtime.rest.messages.checkpoints.CheckpointStatistics;
import org.apache.flink.runtime.rest.messages.checkpoints.CheckpointingStatistics;
import org.apache.flink.runtime.rest.messages.checkpoints.TaskCheckpointStatisticsWithSubtaskDetails;
import org.apache.flink.runtime.rest.messages.job.JobDetailsInfo;
import org.apache.flink.runtime.rest.messages.job.JobExecutionResultResponseBody;
import org.apache.flink.runtime.rest.messages.job.JobSubmitRequestBody;
import org.apache.flink.runtime.rest.messages.job.JobSubmitResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.SavepointInfo;
import org.apache.flink.runtime.rest.messages.job.savepoints.SavepointTriggerRequestBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/jobs")
@Tag(name = "jobs接口")
public class JobController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-overview
     */
    @GetMapping("overview")
    @Operation(summary = "任务概览", description = "任务概览")
    public CompletableFuture<MultipleJobsDetails> jobsOverview() throws IOException {
        return restClient.job().jobsOverview();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-1
     */
    @GetMapping
    @Operation(summary = "任务列表", description = "任务列表")
    public CompletableFuture<JobIdsWithStatusOverview> jobs() throws IOException {
        return restClient.job().jobs();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-metrics
     */
    @GetMapping("/metrics")
    @Operation(summary = "任务 metrics", description = "任务 metrics")
    public CompletableFuture<AggregatedMetricsResponseBody> jobsMetrics(@RequestParam(value = "get", required = false) Optional<String> get,
                                                                        @RequestParam(value = "agg", required = false) Optional<String> agg,
                                                                        @RequestParam(value = "jobs", required = false) Optional<String> jobs) throws IOException {
        return restClient.job().jobsMetric(get, agg, jobs);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid
     */
    @GetMapping("{jobId}")
    @Operation(summary = "任务详情", description = "任务详情")
    public CompletableFuture<JobDetailsInfo> job(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobDetail(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-config
     */
    @GetMapping("{jobId}/config")
    @Operation(summary = "任务配置", description = "任务配置")
    public CompletableFuture<JobConfigInfo> jobConfig(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobConfig(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-metrics
     */
    @GetMapping("{jobId}/metrics")
    @Operation(summary = "任务 metrics", description = "任务 metrics")
    public CompletableFuture<MetricCollectionResponseBody> jobMetrics(@PathVariable("jobId") String jobId,
                                                                      @RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.job().jobMetrics(jobId, get);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-exceptions
     */
    @GetMapping("{jobId}/exceptions")
    @Operation(summary = "任务异常信息", description = "任务异常信息")
    public CompletableFuture<JobExceptionsInfoWithHistory> jobExceptions(@PathVariable("jobId") String jobId,
                                                                         @RequestParam(value = "maxExceptions", required = false) Optional<String> maxExceptions) throws IOException {
        return restClient.job().jobException(jobId, maxExceptions);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-execution-result
     */
    @GetMapping("{jobId}/execution-result")
    @Operation(summary = "任务执行结果", description = "任务执行结果")
    public CompletableFuture<JobExecutionResultResponseBody> jobExecutionResult(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobExecutionResult(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-accumulators
     */
    @GetMapping("{jobId}/accumulators")
    @Operation(summary = "任务 subtask 累加器", description = "任务 subtask 累加器")
    public CompletableFuture<JobAccumulatorsInfo> jobAccumulators(@PathVariable("jobId") String jobId,
                                                                  @RequestParam(value = "includeSerializedValue", required = false) Optional<Boolean> includeSerializedValue) throws IOException {
        return restClient.job().jobAccumulators(jobId, includeSerializedValue);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-plan
     */
    @GetMapping("{jobId}/plan")
    @Operation(summary = "任务 dataflow plan", description = "任务 dataflow plan")
    public CompletableFuture<JobPlanInfo> jobPlan(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobPlan(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-checkpoints
     */
    @GetMapping("{jobId}/checkpoints")
    @Operation(summary = "任务 checkpoints", description = "任务 checkpoints")
    public CompletableFuture<CheckpointingStatistics> jobCheckpoints(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobCheckpoints(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-checkpoints-config
     */
    @GetMapping("{jobId}/checkpoints/config")
    @Operation(summary = "任务 checkpoint 配置", description = "任务 checkpoint 配置")
    public CompletableFuture<CheckpointConfigInfo> jobCheckpointsConfig(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobCheckpointConfig(jobId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-checkpoints-details-checkpointid
     */
    @GetMapping("{jobId}/checkpoints/details/{checkpointId}")
    @Operation(summary = "任务 checkpoint 详情", description = "任务 checkpoint 详情")
    public CompletableFuture<CheckpointStatistics> jobCheckpointDetail(@PathVariable("jobId") String jobId, @PathVariable("checkpointId") Long checkpointId) throws IOException {
        return restClient.job().jobCheckpointDetail(jobId, checkpointId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-checkpoints-details-checkpointid-subtasks-vertexid
     */
    @GetMapping("{jobId}/checkpoints/details/{checkpointId}/subtasks/{vertexId}")
    @Operation(summary = "任务 task 和 subtasks checkpoint 信息", description = "任务 task 和 subtasks checkpoint 信息")
    public CompletableFuture<TaskCheckpointStatisticsWithSubtaskDetails> jobCheckpointSubtaskDetail(@PathVariable("jobId") String jobId,
                                                                                                    @PathVariable("checkpointId") Long checkpointId,
                                                                                                    @PathVariable("vertexId") String vertexId) throws IOException {
        return restClient.job().jobCheckpointSubtaskDetail(jobId, checkpointId, vertexId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-1
     */
    @PostMapping
    @Operation(summary = "提交任务", description = "提交任务")
    public CompletableFuture<JobSubmitResponseBody> submit(@RequestBody JobSubmitRequestBody requestBody) throws IOException {
        return restClient.job().jobSubmit(requestBody);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-1
     */
    @PatchMapping("{jobId}")
    @Operation(summary = "终止任务", description = "终止任务")
    public CompletableFuture<EmptyResponseBody> terminate(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobTerminate(jobId, null);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-stop
     */
    @PostMapping("{jobId}/stop")
    @Operation(summary = "停止任务，并创建一个 savepoint", description = "停止任务，并创建一个 savepoint")
    public CompletableFuture<TriggerResponse> stop(@PathVariable("jobId") String jobId, @RequestBody StopWithSavepointRequestBody requestBody) throws IOException {
        return restClient.job().jobStop(jobId, requestBody);
    }

    /**
     * Rescaling is temporarily disabled. See FLINK-12312
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-rescaling
     */
    @PostMapping("{jobId}/rescaling")
    @Operation(summary = "异步任务缩放", description = "异步任务缩放")
    public CompletableFuture<TriggerResponse> rescale(@PathVariable("jobId") String jobId,
                                                      @RequestParam(value = "parallelism", defaultValue = "2") Integer parallelism) throws IOException {
        return restClient.job().jobRescale(jobId, parallelism);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-rescaling-triggerid
     */
    @GetMapping("{jobId}/rescaling/{triggerId}")
    @Operation(summary = "异步任务缩放结果", description = "异步任务缩放结果")
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> rescaleResult(@PathVariable("jobId") String jobId,
                                                                                                   @PathVariable("triggerId") String triggerId) throws IOException {
        return restClient.job().jobRescaleResult(jobId, triggerId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-savepoints
     */
    @PostMapping("{jobId}/savepoints")
    @Operation(summary = "异步创建 savepoint", description = "异步创建 savepoint")
    public CompletableFuture<TriggerResponse> savepoint(@PathVariable("jobId") String jobId,
                                                        @RequestBody SavepointTriggerRequestBody requestBody) throws IOException {
        return restClient.job().jobSavepoint(jobId, requestBody);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobs-jobid-savepoints-triggerid
     */
    @GetMapping("{jobId}/savepoints/{triggerId}")
    @Operation(summary = "异步创建 savepoint 结果", description = "异步创建 savepoint 结果")
    public CompletableFuture<AsynchronousOperationResult<SavepointInfo>> savepointResult(@PathVariable("jobId") String jobId,
                                                                                         @PathVariable("triggerId") String triggerId) throws IOException {
        return restClient.job().jobSavepointResult(jobId, triggerId);
    }

}
