/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.JobClient;
import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import cn.sliew.milky.common.check.Ensures;
import cn.sliew.milky.common.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.sliew.flinkful.rest.http.FlinkHttpClient.APPLICATION_JSON;

public class JobHttpClient extends AsyncClient implements JobClient {

    private final String webInterfaceURL;

    public JobHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<MultipleJobsDetails> jobsOverview() throws IOException {
        String url = webInterfaceURL + "/jobs/overview";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MultipleJobsDetails.class);
    }

    @Override
    public CompletableFuture<AggregatedMetricsResponseBody> jobsMetric(Optional<String> get, Optional<String> agg, Optional<String> jobs) throws IOException {
        String url = webInterfaceURL + "/jobs/metrics";
        List<String> queryParams = new LinkedList<>();
        if (get.isPresent()) {
            queryParams.add("get=" + get.get());
        }
        if (agg.isPresent()) {
            queryParams.add("agg=" + agg.get());
        }
        if (jobs.isPresent()) {
            queryParams.add("jobs=" + jobs.get());
        }
        if (queryParams.isEmpty() == false) {
            String params = queryParams.stream().collect(Collectors.joining("&"));
            url = url + "?" + params;
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, AggregatedMetricsResponseBody.class);
    }

    @Override
    public CompletableFuture<JobIdsWithStatusOverview> jobs() throws IOException {
        String url = webInterfaceURL + "/jobs";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobIdsWithStatusOverview.class);
    }

    @Override
    public CompletableFuture<JobDetailsInfo> jobDetail(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobDetailsInfo.class);
    }

    @Override
    public CompletableFuture<JobSubmitResponseBody> jobSubmit(JobSubmitRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/jobs";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, JobSubmitResponseBody.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> jobTerminate(String jobId, String mode) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId;
        if (StringUtils.isNotBlank(mode)) {
            url = url + "?mode=" + mode;
        }
        Request request = new Request.Builder()
                .patch(Util.EMPTY_REQUEST)
                .url(url)
                .build();
        return remoteCall(request);
    }

    @Override
    public CompletableFuture<JobAccumulatorsInfo> jobAccumulators(String jobId, Optional<Boolean> includeSerializedValue) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/accumulators";
        if (includeSerializedValue.isPresent()) {
            url = url + "?includeSerializedValue=" + includeSerializedValue.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobAccumulatorsInfo.class);
    }

    @Override
    public CompletableFuture<CheckpointingStatistics> jobCheckpoints(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/checkpoints";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, CheckpointingStatistics.class);
    }

    @Override
    public CompletableFuture<CheckpointConfigInfo> jobCheckpointConfig(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/checkpoints/config";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, CheckpointConfigInfo.class);
    }

    @Override
    public CompletableFuture<CheckpointStatistics> jobCheckpointDetail(String jobId, Long checkpointId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/checkpoints/details/" + checkpointId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, CheckpointStatistics.class);
    }

    @Override
    public CompletableFuture<TaskCheckpointStatisticsWithSubtaskDetails> jobCheckpointSubtaskDetail(String jobId, Long checkpointId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/checkpoints/details/" + checkpointId + "/subtasks/" + vertexId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, TaskCheckpointStatisticsWithSubtaskDetails.class);
    }

    @Override
    public CompletableFuture<JobConfigInfo> jobConfig(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/config";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobConfigInfo.class);
    }

    @Override
    public CompletableFuture<JobExceptionsInfoWithHistory> jobException(String jobId, Optional<String> maxExceptions) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/exceptions";
        if (maxExceptions.isPresent()) {
            url = url + "?maxExceptions=" + maxExceptions.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobExceptionsInfoWithHistory.class);
    }

    @Override
    public CompletableFuture<JobExecutionResultResponseBody> jobExecutionResult(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/execution-result";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobExecutionResultResponseBody.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobMetrics(String jobId, Optional<String> get) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/metrics";
        if (get.isPresent()) {
            url = url + "?get=" + get.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }

    @Override
    public CompletableFuture<JobPlanInfo> jobPlan(String jobId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/plan";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobPlanInfo.class);
    }

    @Override
    public CompletableFuture<TriggerResponse> jobRescale(String jobId, Integer parallelism) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/rescaling";
        Ensures.checkNotNull(parallelism, () -> "parallelism can't be null");
        Ensures.checkArgument(parallelism > 0, () -> "parallelism must be positive integer");
        Request request = new Request.Builder()
                .patch(Util.EMPTY_REQUEST)
                .url(url)
                .build();
        return remoteCall(request, TriggerResponse.class);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> jobRescaleResult(String jobId, String triggerId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/rescaling/" + triggerId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, AsynchronousOperationResult.class, AsynchronousOperationInfo.class).thenApply(result -> {
            AsynchronousOperationResult<AsynchronousOperationInfo> type = result;
            return type;
        });
    }

    @Override
    public CompletableFuture<TriggerResponse> jobSavepoint(String jobId, SavepointTriggerRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/savepoints";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, TriggerResponse.class);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<SavepointInfo>> jobSavepointResult(String jobId, String triggerId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/savepoints/" + triggerId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, AsynchronousOperationResult.class, SavepointInfo.class).thenApply(result -> {
            AsynchronousOperationResult<SavepointInfo> type = result;
            return type;
        });
    }

    @Override
    public CompletableFuture<TriggerResponse> jobStop(String jobId, StopWithSavepointRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/stop";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, TriggerResponse.class);
    }
}
