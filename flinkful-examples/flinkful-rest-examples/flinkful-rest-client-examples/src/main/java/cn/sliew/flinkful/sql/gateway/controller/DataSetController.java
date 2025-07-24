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

import cn.sliew.flinkful.rest.base.v1.client.DataSetClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/datasets")
@Tag(name = "DataSet接口")
public class DataSetController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#datasets
     */
    @GetMapping
    @Operation(summary = "data set 列表", description = "data set 列表")
    public CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.datasets();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#datasets-datasetid
     */
    @DeleteMapping("/{dataSetId}")
    @Operation(summary = "异步删除 data set", description = "异步删除 data set")
    public CompletableFuture<TriggerResponse> delete(@PathVariable("dataSetId") String dataSetId) throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.deleteDataSet(dataSetId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#datasets-delete-triggerid
     */
    @GetMapping("/delete/{triggerId}")
    @Operation(summary = "异步删除 data set 结果", description = "异步删除 data set 结果")
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteStatus(@PathVariable("triggerId") String triggerId) throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.deleteDataSetStatus(triggerId);
    }
}
