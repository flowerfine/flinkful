package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.SqlGateWayClient;
import cn.sliew.flinkful.rest.client.param.ExecuteStatementParam;
import cn.sliew.flinkful.rest.client.param.OpenSessionParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flink/sql/gateway")
@Api(value = "/gateway", tags = "SQL gateway接口")
public class SqlGateWayContorller {

    @Autowired
    private SqlGateWayClient sqlGateWayClient;

    @GetMapping("api_versions")
    @ApiOperation("Get the current available versions for the Rest Endpoint. " +
            "The client can choose one of the return version as the protocol for later communicate.")
    public CompletableFuture<GetApiVersionResponseBody> getApiVersion() throws IOException {
        return sqlGateWayClient.sqlProcess().getApiVersion();
    }

    @GetMapping("info")
    @ApiOperation("Get meta data for this cluster.")
    public CompletableFuture<GetInfoResponseBody> getInfo() throws IOException {
        return sqlGateWayClient.sqlProcess().getInfo();
    }

    @PostMapping
    @ApiOperation("Opens a new session with specific properties. " +
            "Specific properties can be given for current session which will override the default properties of gateway.")
    public CompletableFuture<OpenSessionResponseBody> openSession(@Valid OpenSessionParam param) throws IOException {
        OpenSessionRequestBody body = new OpenSessionRequestBody(param.getSessionName(), param.getProperties());
        return sqlGateWayClient.sqlProcess().openSession(body);
    }

    @GetMapping("{sessionHandle}")
    @ApiOperation("Get the session configuration.")
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().getSessionConfig(sessionHandle);
    }

    @PostMapping("{sessionHandle}/heartbeat")
    @ApiOperation("Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.")
    public CompletableFuture<EmptyResponseBody> heartbeat(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().heartbeat(sessionHandle);
    }

    @DeleteMapping("{sessionHandle}")
    @ApiOperation("Closes the specific session.")
    public CompletableFuture<CloseSessionResponseBody> closeSession(@PathVariable("sessionHandle") String sessionHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().closeSession(sessionHandle);
    }

    @PostMapping("{sessionHandle}/statements")
    @ApiOperation("Execute a statement.")
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(@PathVariable("sessionHandle") String sessionHandle,
                                                                            @Valid @RequestBody ExecuteStatementParam param) throws IOException {
        ExecuteStatementRequestBody body = new ExecuteStatementRequestBody(param.getStatement(), param.getTimeout(), param.getExecutionConfig());
        return sqlGateWayClient.sqlProcess().executeStatement(sessionHandle, body);
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/result/{token}")
    @ApiOperation("Fetch results of Operation.")
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle,
                                                                          @PathVariable("token") String token) throws IOException {
        return sqlGateWayClient.sqlProcess().getStatementResult(sessionHandle, operationHandle, token);
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/status")
    @ApiOperation("Get the status of operation.")
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(@PathVariable("sessionHandle") String sessionHandle,
                                                                             @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().getOperationStatus(sessionHandle, operationHandle);
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/cancel")
    @ApiOperation("Cancel the operation.")
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().cancelOperation(sessionHandle, operationHandle);
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/close")
    @ApiOperation("Close the operation.")
    public CompletableFuture<OperationStatusResponseBody> closeOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                         @PathVariable("operationHandle") String operationHandle) throws IOException {
        return sqlGateWayClient.sqlProcess().closeOperation(sessionHandle, operationHandle);
    }

}
