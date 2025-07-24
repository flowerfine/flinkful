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
import org.apache.flink.runtime.rest.messages.ConfigurationInfo;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/job-manager")
@Tag(name = "JobManager接口")
public class JobManagerController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobmanager-config
     */
    @GetMapping("config")
    @Operation(summary = "JobManager 配置", description = "JobManager 配置")
    public CompletableFuture<ConfigurationInfo> config() throws IOException {
        return restClient.jobManager().jobmanagerConfig();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobmanager-logs
     */
    @GetMapping("logs")
    @Operation(summary = "JobManager 日志文件", description = "JobManager 日志文件")
    public CompletableFuture<LogListInfo> logs() throws IOException {
        return restClient.jobManager().jobmanagerLogs();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jobmanager-metrics
     */
    @GetMapping("metrics")
    @Operation(summary = "JobManager metrics", description = "JobManager metrics")
    public CompletableFuture<MetricCollectionResponseBody> metrics(@RequestParam(value = "get", required = false) Optional<String> get) throws IOException {
        return restClient.jobManager().jobmanagerMetrics(get);
    }
}
