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
package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.JarClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.JobPlanInfo;
import org.apache.flink.runtime.webmonitor.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/jars")
@Tag(name = "jars接口")
public class JarController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jars
     */
    @GetMapping
    @Operation(summary = "上传 jars 列表", description = "上传 jars 列表")
    public CompletableFuture<JarListInfo> jars() throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jars();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jars-upload
     */
    @PostMapping("upload")
    @Operation(summary = "上传 jar", description = "上传 jar")
    public CompletableFuture<JarUploadResponseBody> upload(@RequestParam("filePath") String filePath) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.uploadJar(filePath);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jars-jarid
     */
    @DeleteMapping("{jarId}")
    @Operation(summary = "删除 jar", description = "删除 jar")
    public CompletableFuture<EmptyResponseBody> delete(@PathVariable("jarId") String jarId) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.deleteJar(jarId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jars-jarid-plan
     */
    @GetMapping("{jarId}/plan")
    @Operation(summary = "查看 jar 的 dataflow plan", description = "查看 jar 的 dataflow plan")
    public CompletableFuture<JobPlanInfo> jarPlan(@PathVariable("jarId") String jarId, JarPlanRequestBody requestBody) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jarPlan(jarId, requestBody);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#jars-jarid-run
     */
    @PostMapping("{jarId}/run")
    @Operation(summary = "提交 jar 到集群中运行", description = "提交 jar 到集群中运行")
    public CompletableFuture<JarRunResponseBody> jarRun(@PathVariable("jarId") String jarId, @RequestBody JarRunRequestBody requestBody) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jarRun(jarId, requestBody);
    }
}
