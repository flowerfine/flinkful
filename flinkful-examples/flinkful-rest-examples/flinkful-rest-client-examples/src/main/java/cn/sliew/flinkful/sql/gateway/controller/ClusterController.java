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

import cn.sliew.flinkful.rest.base.v1.client.ClusterClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/cluster")
@Tag(name = "集群接口")
public class ClusterController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#overview-1
     */
    @GetMapping("overview")
    @Operation(summary = "集群概况", description = "集群概况")
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.overview();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#cluster
     */
    @DeleteMapping
    @Operation(summary = "关闭集群", description = "关闭集群")
    public CompletableFuture<EmptyResponseBody> shutdown() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.shutdownCluster();
    }
}
