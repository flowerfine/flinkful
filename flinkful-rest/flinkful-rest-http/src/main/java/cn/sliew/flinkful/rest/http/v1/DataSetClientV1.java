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
package cn.sliew.flinkful.rest.http.v1;

import cn.sliew.flinkful.rest.base.v1.messages.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.rest.base.v1.messages.async.AsynchronousOperationResult;
import cn.sliew.flinkful.rest.base.v1.messages.async.TriggerResponse;
import cn.sliew.flinkful.rest.base.v1.messages.dataset.ClusterDataSetList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface DataSetClientV1 {

    @RequestLine("GET v1/datasets")
    @Headers("Content-Type: application/json")
    ClusterDataSetList datasets();

    @RequestLine("DELETE v1/datasets/{datasetId}")
    @Headers("Content-Type: application/json")
    TriggerResponse deleteDataSet(@Param("datasetId") String datasetId);

    @RequestLine("GET v1/datasets/delete/{triggerId}")
    @Headers("Content-Type: application/json")
    AsynchronousOperationResult<AsynchronousOperationInfo> deleteDataSetStatus(@Param("triggerId") String triggerId);
}
