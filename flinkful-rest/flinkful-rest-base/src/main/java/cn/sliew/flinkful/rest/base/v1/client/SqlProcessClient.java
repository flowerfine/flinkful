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

import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.CloseSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.session.GetSessionConfigResponseBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionRequestBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.*;
import org.apache.flink.table.gateway.rest.message.util.GetApiVersionResponseBody;
import org.apache.flink.table.gateway.rest.message.util.GetInfoResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface SqlProcessClient {

    CompletableFuture<GetApiVersionResponseBody> getApiVersion() throws IOException;

    CompletableFuture<GetInfoResponseBody> getInfo() throws IOException;

    CompletableFuture<OpenSessionResponseBody> openSession(OpenSessionRequestBody requestBody) throws IOException;

    CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle) throws IOException;

    CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle) throws IOException;

    CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle) throws IOException;

    CompletableFuture<CompleteStatementResponseBody> completeStatement(String sessionHandle, CompleteStatementRequestBody requestBody) throws IOException;

    CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle, ExecuteStatementRequestBody requestBody) throws IOException;

    CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle, String operationHandle, String token, String rowFormat) throws IOException;

    CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle, String operationHandle) throws IOException;

    CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle, String operationHandle) throws IOException;

    CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle,
                                                                  String operationHandle) throws IOException;

}
