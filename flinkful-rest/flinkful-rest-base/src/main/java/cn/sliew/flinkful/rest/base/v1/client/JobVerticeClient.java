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

import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptDetailsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtasksAllAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.webmonitor.threadinfo.VertexFlameGraph;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface JobVerticeClient {

    /**
     * Returns details for a task, with a summary for each of its subtasks.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<JobVertexDetailsInfo> jobVertexDetail(String jobId, String vertexId) throws IOException;

    /**
     * Returns user-defined accumulators of a task, aggregated across all subtasks.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<JobVertexAccumulatorsInfo> jobVertexAccumulators(String jobId, String vertexId) throws IOException;

    /**
     * Returns back-pressure information for a job, and may initiate back-pressure sampling if necessary.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<JobVertexBackPressureInfo> jobVertexBackPressure(String jobId, String vertexId) throws IOException;

    /**
     * Returns flame graph information for a vertex, and may initiate flame graph sampling if necessary.
     *
     * @param jobId          32-character hexadecimal string value that identifies a job.
     * @param vertexId       32-character hexadecimal string value that identifies a job vertex.
     * @param type(optional) String value that specifies the Flame Graph type. Supported options are: "[FULL, ON_CPU, OFF_CPU]".
     */
    CompletableFuture<VertexFlameGraph> jobVertexFlameGraph(String jobId, String vertexId, Optional<String> type) throws IOException;

    /**
     * Provides access to task metrics.
     *
     * @param jobId         32-character hexadecimal string value that identifies a job.
     * @param vertexId      32-character hexadecimal string value that identifies a job vertex.
     * @param get(optional) Comma-separated list of string values to select specific metrics.
     */
    CompletableFuture<MetricCollectionResponseBody> jobVertexMetrics(String jobId, String vertexId, Optional<String> get) throws IOException;

    /**
     * Returns time-related information for all subtasks of a task.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<SubtasksTimesInfo> jobVertexSubtaskTimes(String jobId, String vertexId) throws IOException;

    /**
     * Returns task information aggregated by task manager.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<JobVertexTaskManagersInfo> jobVertexTaskManagers(String jobId, String vertexId) throws IOException;

    /**
     * Returns the watermarks for all subtasks of a task.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<MetricCollectionResponseBody> jobVertexWatermarks(String jobId, String vertexId) throws IOException;

    /**
     * Returns all user-defined accumulators for all subtasks of a task.
     *
     * @param jobId    32-character hexadecimal string value that identifies a job.
     * @param vertexId 32-character hexadecimal string value that identifies a job vertex.
     */
    CompletableFuture<SubtasksAllAccumulatorsInfo> jobVertexSubtaskAccumulators(String jobId, String vertexId) throws IOException;

    /**
     * Provides access to aggregated subtask metrics.
     *
     * @param jobId              32-character hexadecimal string value that identifies a job.
     * @param vertexId           32-character hexadecimal string value that identifies a job vertex.
     * @param get(optional)      Comma-separated list of string values to select specific metrics.
     * @param agg(optional)      Comma-separated list of aggregation modes which should be calculated. Available aggregations are: "min, max, sum, avg".
     * @param subtasks(optional) Comma-separated list of integer ranges (e.g. "1,3,5-9") to select specific subtasks.
     */
    CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Optional<String> get, Optional<String> agg, Optional<String> subtasks) throws IOException;

    /**
     * Returns details of the current or latest execution attempt of a subtask.
     *
     * @param jobId        32-character hexadecimal string value that identifies a job.
     * @param vertexId     32-character hexadecimal string value that identifies a job vertex.
     * @param subtaskindex Positive integer value that identifies a subtask.
     */
    CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskDetail(String jobId, String vertexId, Integer subtaskindex) throws IOException;

    /**
     * Provides access to subtask metrics.
     *
     * @param jobId         32-character hexadecimal string value that identifies a job.
     * @param vertexId      32-character hexadecimal string value that identifies a job vertex.
     * @param subtaskindex  Positive integer value that identifies a subtask.
     * @param get(optional) Comma-separated list of string values to select specific metrics.
     */
    CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Integer subtaskindex, String get) throws IOException;

    /**
     * Returns details of an execution attempt of a subtask.
     * Multiple execution attempts happen in case of failure/recovery.
     *
     * @param jobId        32-character hexadecimal string value that identifies a job.
     * @param vertexId     32-character hexadecimal string value that identifies a job vertex.
     * @param subtaskindex Positive integer value that identifies a subtask.
     * @param attempt      Positive integer value that identifies an execution attempt.
     */
    CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskAttemptDetail(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException;

    /**
     * Returns the accumulators of an execution attempt of a subtask.
     * Multiple execution attempts happen in case of failure/recovery.
     *
     * @param jobId        32-character hexadecimal string value that identifies a job.
     * @param vertexId     32-character hexadecimal string value that identifies a job vertex.
     * @param subtaskindex Positive integer value that identifies a subtask.
     * @param attempt      Positive integer value that identifies an execution attempt.
     */
    CompletableFuture<SubtaskExecutionAttemptAccumulatorsInfo> jobVertexSubtaskAttemptAccumulators(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException;



}
