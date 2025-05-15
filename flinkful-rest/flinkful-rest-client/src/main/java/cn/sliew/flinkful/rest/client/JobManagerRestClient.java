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

import cn.sliew.flinkful.rest.base.v1.client.JobManagerClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.cluster.JobManagerLogListHeaders;
import org.apache.flink.runtime.rest.messages.cluster.JobManagerThreadDumpHeaders;
import org.apache.flink.runtime.rest.messages.job.metrics.JobManagerMetricsHeaders;
import org.apache.flink.runtime.rest.messages.job.metrics.JobManagerMetricsMessageParameters;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class JobManagerRestClient implements JobManagerClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public JobManagerRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<ConfigurationInfo> jobmanagerConfig() throws IOException {
        return client.sendRequest(address, port, ClusterConfigurationInfoHeaders.getInstance());
    }

    @Override
    public CompletableFuture<LogListInfo> jobmanagerLogs() throws IOException {
        return client.sendRequest(address, port, JobManagerLogListHeaders.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobmanagerMetrics(Optional<String> get) throws IOException {
        JobManagerMetricsMessageParameters parameters = new JobManagerMetricsMessageParameters();
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        return client.sendRequest(address, port, JobManagerMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<ThreadDumpInfo> jobmanagerThreadDump() throws IOException {
        return client.sendRequest(address, port, JobManagerThreadDumpHeaders.getInstance(), EmptyMessageParameters.getInstance(), EmptyRequestBody.getInstance());
    }
}
