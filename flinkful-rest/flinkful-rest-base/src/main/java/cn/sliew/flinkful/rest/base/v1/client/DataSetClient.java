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

import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface DataSetClient {

    /**
     * Returns all cluster data sets.
     */
    CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException;

    /**
     * Triggers the deletion of a cluster data set.
     * This async operation would return a 'triggerid' for further query identifier.
     *
     * @param datasetId - 32-character hexadecimal string value that identifies a cluster data set.
     */
    CompletableFuture<TriggerResponse> deleteDataSet(String datasetId) throws IOException;

    /**
     * Returns the status for the delete operation of a cluster data set.
     *
     * @param triggerId - 32-character hexadecimal string that identifies an asynchronous operation trigger ID. The ID was returned then the operation was triggered.
     */
    CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteDataSetStatus(String triggerId) throws IOException;

}
