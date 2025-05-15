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

import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TaskManagerClient {

    /**
     * Returns an overview over all task managers.
     */
    CompletableFuture<TaskManagersInfo> taskManagers() throws IOException;

    /**
     * Provides access to aggregated task manager metrics.
     *
     * @param get(optional)          Comma-separated list of string values to select specific metrics.
     * @param agg(optional)          Comma-separated list of aggregation modes which should be calculated. Available aggregations are: "min, max, sum, avg".
     * @param taskmanagers(optional) Comma-separated list of 32-character hexadecimal strings to select specific task managers.
     */
    CompletableFuture<AggregatedMetricsResponseBody> taskManagersMetrics(Optional<String> get, Optional<String> agg, Optional<String> taskmanagers) throws IOException;

    /**
     * Returns details for a task manager.
     * "metrics.memorySegmentsAvailable" and "metrics.memorySegmentsTotal" are deprecated.
     * Please use "metrics.nettyShuffleMemorySegmentsAvailable" and "metrics.nettyShuffleMemorySegmentsTotal" instead.
     *
     * @param taskManagerId 32-character hexadecimal string that identifies a task manager.
     */
    CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(String taskManagerId) throws IOException;

    /**
     * Returns the list of log files on a TaskManager.
     *
     * @param taskManagerId 32-character hexadecimal string that identifies a task manager.
     */
    CompletableFuture<LogListInfo> taskManagerLogs(String taskManagerId) throws IOException;

    /**
     * Provides access to task manager metrics.
     *
     * @param taskManagerId 32-character hexadecimal string that identifies a task manager.
     * @param get(optional) Comma-separated list of string values to select specific metrics.
     */
    CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(String taskManagerId, Optional<String> get) throws IOException;

    /**
     * Returns the thread dump of the requested TaskManager.
     *
     * @param taskManagerId 32-character hexadecimal string that identifies a task manager.
     */
    CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(String taskManagerId) throws IOException;

}
