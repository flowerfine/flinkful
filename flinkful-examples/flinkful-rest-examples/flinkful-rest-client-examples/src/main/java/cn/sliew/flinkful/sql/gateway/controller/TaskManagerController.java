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
package cn.sliew.flinkful.sql.gateway.controller;

import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/taskmanagers")
@Tag(name = "TaskManager接口")
public class TaskManagerController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers
     */
    @GetMapping
    @Operation(summary = "taskmanagers 概览", description = "taskmanagers 概览")
    public CompletableFuture<TaskManagersInfo> taskManagers() throws IOException {
        return restClient.taskManager().taskManagers();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers-metrics
     */
    @GetMapping("metrics")
    @Operation(summary = "taskmanagers metrics", description = "taskmanagers metrics")
    public CompletableFuture<AggregatedMetricsResponseBody> taskMangersMetrics(@RequestParam(value = "get", required = false) Optional<String> get,
                                                                               @RequestParam(value = "agg", required = false) Optional<String> agg,
                                                                               @RequestParam(value = "taskmanagers", required = false) Optional<String> taskmanagers) throws IOException {
        return restClient.taskManager().taskManagersMetrics(get, agg, taskmanagers);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers-taskmanagerid
     */
    @GetMapping("{taskManagerId}")
    @Operation(summary = "TaskManager 详情", description = "TaskManager 详情")
    public CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerDetail(taskManagerId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers-taskmanagerid-metrics
     */
    @GetMapping("{taskManagerId}/metrics")
    @Operation(summary = "taskmanagers metrics", description = "taskmanagers metrics")
    public CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(@PathVariable("taskManagerId") String taskManagerId,
                                                                              @RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.taskManager().taskManagerMetrics(taskManagerId, get);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers-taskmanagerid-logs
     */
    @GetMapping("{taskManagerId}/logs")
    @Operation(summary = "TaskManager 日志文件", description = "TaskManager 日志文件")
    public CompletableFuture<LogListInfo> taskManagerLogs(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerLogs(taskManagerId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#taskmanagers-taskmanagerid-thread-dump
     */
    @GetMapping("{taskManagerId}/thread-dump")
    @Operation(summary = "dump TaskManager thread", description = "dump TaskManager thread")
    public CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(@PathVariable("taskManagerId") String taskManagerId) throws IOException {
        return restClient.taskManager().taskManagerThreadDump(taskManagerId);
    }
}
