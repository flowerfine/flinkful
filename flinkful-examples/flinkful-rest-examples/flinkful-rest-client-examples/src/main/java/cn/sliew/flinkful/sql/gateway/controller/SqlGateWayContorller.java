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

import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.client.param.ExecuteStatementParam;
import cn.sliew.flinkful.rest.client.param.OpenSessionParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.CloseSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.session.GetSessionConfigResponseBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionRequestBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementRequestBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.FetchResultsResponseBody;
import org.apache.flink.table.gateway.rest.message.util.GetApiVersionResponseBody;
import org.apache.flink.table.gateway.rest.message.util.GetInfoResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flinkful/sql/gateway")
@Tag(name = "SQL gateway接口")
public class SqlGateWayContorller {

    @Autowired
    private SqlGatewayClient sqlGateWayClient;

    @GetMapping("api_versions")
    @Operation(summary = "api versions", description = "Get the current available versions for the Rest Endpoint. " +
            "The client can choose one of the return version as the protocol for later communicate.")
    public CompletableFuture<GetApiVersionResponseBody> getApiVersion() throws IOException {
        return sqlGateWayClient.sqlProcess().getApiVersion();
    }

    @GetMapping("info")
    @Operation(summary = "info", description = "Get meta data for this cluster.")
    public CompletableFuture<GetInfoResponseBody> getInfo() throws IOException {
        return sqlGateWayClient.sqlProcess().getInfo();
    }

    @PostMapping
    @Operation(summary = "open session", description = "Opens a new session with specific properties. " +
            "Specific properties can be given for current session which will override the default properties of gateway.")
    public CompletableFuture<OpenSessionResponseBody> openSession(@Valid OpenSessionParam param) throws IOException {
        OpenSessionRequestBody body = new OpenSessionRequestBody(param.getSessionName(), param.getProperties());
        return sqlGateWayClient.sqlProcess().openSession(body);
    }

    @GetMapping("{sessionHandle}")
    @Operation(summary = "get session config", description = "Get the session configuration.")
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().getSessionConfig(sessionHandle);
    }

    @PostMapping("{sessionHandle}/heartbeat")
    @Operation(summary = "heartbeat", description = "Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.")
    public CompletableFuture<EmptyResponseBody> heartbeat(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().heartbeat(sessionHandle);
    }

    @DeleteMapping("{sessionHandle}")
    @Operation(summary = "close session", description = "Closes the specific session.")
    public CompletableFuture<CloseSessionResponseBody> closeSession(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().closeSession(sessionHandle);
    }

    @PostMapping("{sessionHandle}/statements")
    @Operation(summary = "execute statement", description = "Execute a statement.")
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(@PathVariable("sessionHandle") String sessionHandle,
                                                                            @Valid @RequestBody ExecuteStatementParam param) throws IOException {
        ExecuteStatementRequestBody body = new ExecuteStatementRequestBody(param.getStatement(), param.getTimeout(), param.getExecutionConfig());
        return sqlGateWayClient.sqlProcess().executeStatement(sessionHandle, body);
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/result/{token}")
    @Operation(summary = "fetch statement result", description = "Fetch results of Operation.")
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle,
                                                                          @PathVariable("token") String token,
                                                                          @RequestParam("rowFormat") String rowFormat) throws IOException {
        return sqlGateWayClient.sqlProcess().getStatementResult(sessionHandle, operationHandle, token, rowFormat);
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/status")
    @Operation(summary = "get operation status", description = "Get the status of operation.")
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(@PathVariable("sessionHandle") String sessionHandle,
                                                                             @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().getOperationStatus(sessionHandle, operationHandle);
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/cancel")
    @Operation(summary = "cancel operation", description = "Cancel the operation.")
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().cancelOperation(sessionHandle, operationHandle);
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/close")
    @Operation(summary = "close operation", description = "Close the operation.")
    public CompletableFuture<OperationStatusResponseBody> closeOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                         @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().closeOperation(sessionHandle, operationHandle);
    }

}
