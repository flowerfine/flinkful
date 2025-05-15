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
package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.v1.client.JobVerticeClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.job.*;
import org.apache.flink.runtime.rest.messages.job.metrics.*;
import org.apache.flink.runtime.webmonitor.threadinfo.VertexFlameGraph;

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
    public CompletableFuture<VertexFlameGraph> jobVertexFlameGraph(String jobId, String vertexId, Optional<String> type) throws IOException {
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
