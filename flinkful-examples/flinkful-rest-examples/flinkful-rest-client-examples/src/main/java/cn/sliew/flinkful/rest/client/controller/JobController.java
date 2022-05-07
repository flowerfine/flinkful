package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flink/jobs")
@Api(value = "/jobs", tags = "jobs接口")
public class JobController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#jobs-overview
     */
    @GetMapping("overview")
    @ApiOperation("")
    public CompletableFuture<MultipleJobsDetails> jobsOverview() throws IOException {
        return restClient.job().jobsOverview();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#jobs-1
     */
    @GetMapping
    @ApiOperation("")
    public CompletableFuture<JobIdsWithStatusOverview> jobs() throws IOException {
        return restClient.job().jobs();
    }

    @GetMapping("/metrics")
    @ApiOperation("")
    public CompletableFuture<AggregatedMetricsResponseBody> jobsMetrics(@RequestParam(value = "get", required = false) Optional<String> get,
                                                                        @RequestParam(value = "agg", required = false) Optional<String> agg,
                                                                        @RequestParam(value = "jobs", required = false) Optional<String> jobs) throws IOException {
        return restClient.job().jobsMetric(get, agg, jobs);
    }

    @GetMapping("{jobId}")
    @ApiOperation("")
    public CompletableFuture<JobDetailsInfo> job(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobDetail(jobId);
    }

    @GetMapping("{jobId}/config")
    @ApiOperation("")
    public CompletableFuture<JobConfigInfo> jobConfig(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobConfig(jobId);
    }

    @GetMapping("{jobId}/metrics")
    @ApiOperation("")
    public CompletableFuture<MetricCollectionResponseBody> jobMetrics(@PathVariable("jobId") String jobId,
                                                                      @RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.job().jobMetrics(jobId, get);
    }

    @GetMapping("{jobId}/exceptions")
    @ApiOperation("")
    public CompletableFuture<JobExceptionsInfoWithHistory> jobExceptions(@PathVariable("jobId") String jobId,
                                                                         @RequestParam(value = "maxExceptions", required = false) Optional<String> maxExceptions) throws IOException {
        return restClient.job().jobException(jobId, maxExceptions);
    }

    @GetMapping("{jobId}/execution-result")
    @ApiOperation("")
    public CompletableFuture<JobExecutionResultResponseBody> jobExecutionResult(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobExecutionResult(jobId);
    }

    @GetMapping("{jobId}/accumulators")
    @ApiOperation("")
    public CompletableFuture<JobAccumulatorsInfo> jobAccumulators(@PathVariable("jobId") String jobId,
                                                                  @RequestParam(value = "includeSerializedValue", required = false) Optional<Boolean> includeSerializedValue) throws IOException {
        return restClient.job().jobAccumulators(jobId, includeSerializedValue);
    }

    @GetMapping("{jobId}/plan")
    @ApiOperation("")
    public CompletableFuture<JobPlanInfo> jobPlan(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobPlan(jobId);
    }

    @GetMapping("{jobId}/checkpoints")
    @ApiOperation("")
    public CompletableFuture<CheckpointingStatistics> jobCheckpoints(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobCheckpoints(jobId);
    }

    @GetMapping("{jobId}/checkpoints/config")
    @ApiOperation("")
    public CompletableFuture<CheckpointConfigInfo> jobCheckpointsConfig(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobCheckpointConfig(jobId);
    }

    @GetMapping("{jobId}/checkpoints/details/{checkpointId}")
    @ApiOperation("")
    public CompletableFuture<CheckpointStatistics> jobCheckpointDetail(@PathVariable("jobId") String jobId, @PathVariable("checkpointId") Long checkpointId) throws IOException {
        return restClient.job().jobCheckpointDetail(jobId, checkpointId);
    }

    @GetMapping("{jobId}/checkpoints/details/{checkpointId}/subtasks/{vertexId}")
    @ApiOperation("")
    public CompletableFuture<TaskCheckpointStatisticsWithSubtaskDetails> jobCheckpointSubtaskDetail(@PathVariable("jobId") String jobId,
                                                                                                    @PathVariable("checkpointId") Long checkpointId,
                                                                                                    @PathVariable("vertexId") String vertexId) throws IOException {
        return restClient.job().jobCheckpointSubtaskDetail(jobId, checkpointId, vertexId);
    }

    @PostMapping("/")
    @ApiOperation("")
    public CompletableFuture<JobSubmitResponseBody> submit(@RequestBody JobSubmitRequestBody requestBody) throws IOException {
        return restClient.job().jobSubmit(requestBody);
    }

    @PatchMapping("{jobId}")
    @ApiOperation("")
    public CompletableFuture<EmptyResponseBody> terminate(@PathVariable("jobId") String jobId) throws IOException {
        return restClient.job().jobTerminate(jobId, null);
    }

    @PostMapping("{jobId}/stop")
    @ApiOperation("")
    public CompletableFuture<TriggerResponse> stop(@PathVariable("jobId") String jobId, @RequestBody StopWithSavepointRequestBody requestBody) throws IOException {
        return restClient.job().jobStop(jobId, requestBody);
    }

    /**
     * Rescaling is temporarily disabled. See FLINK-12312
     */
    @PostMapping("{jobId}/rescaling")
    @ApiOperation("")
    public CompletableFuture<TriggerResponse> rescale(@PathVariable("jobId") String jobId,
                                                      @RequestParam(value = "parallelism", defaultValue = "2") Integer parallelism) throws IOException {
        return restClient.job().jobRescale(jobId, parallelism);
    }

    @GetMapping("{jobId}/rescaling/{triggerId}")
    @ApiOperation("")
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> rescaleResult(@PathVariable("jobId") String jobId,
                                                                                                   @PathVariable("triggerId") String triggerId) throws IOException {
        return restClient.job().jobRescaleResult(jobId, triggerId);
    }

    @PostMapping("{jobId}/savepoints")
    @ApiOperation("")
    public CompletableFuture<TriggerResponse> savepoint(@PathVariable("jobId") String jobId,
                                                        @RequestBody SavepointTriggerRequestBody requestBody) throws IOException {
        return restClient.job().jobSavepoint(jobId, requestBody);
    }

    @GetMapping("{jobId}/savepoints/{triggerId}")
    @ApiOperation("")
    public CompletableFuture<AsynchronousOperationResult<SavepointInfo>> savepointResult(@PathVariable("jobId") String jobId,
                                                                                         @PathVariable("triggerId") String triggerId) throws IOException {
        return restClient.job().jobSavepointResult(jobId, triggerId);
    }

}
