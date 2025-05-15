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
package cn.sliew.flinkful.rest.base.v1.client;

import org.apache.flink.runtime.rest.messages.ConfigurationInfo;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface JobManagerClient {

    /**
     * Returns the cluster configuration.
     */
    CompletableFuture<ConfigurationInfo> jobmanagerConfig() throws IOException;

    /**
     * Returns the list of log files on the JobManager.
     */
    CompletableFuture<LogListInfo> jobmanagerLogs() throws IOException;

    /**
     * Provides access to job manager metrics.
     *
     * @param get(optional) Comma-separated list of string values to select specific metrics.
     */
    CompletableFuture<MetricCollectionResponseBody> jobmanagerMetrics(Optional<String> get) throws IOException;

    /**
     * Returns the thread dump of the JobManager.
     */
    CompletableFuture<ThreadDumpInfo> jobmanagerThreadDump() throws IOException;

}
