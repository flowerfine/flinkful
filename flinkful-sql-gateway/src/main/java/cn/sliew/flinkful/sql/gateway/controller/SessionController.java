package cn.sliew.flinkful.sql.gateway.controller;

import cn.sliew.flinkful.sql.gateway.controller.param.ExecuteStatementParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.table.gateway.rest.header.operation.CancelOperationHeaders;
import org.apache.flink.table.gateway.rest.header.operation.CloseOperationHeaders;
import org.apache.flink.table.gateway.rest.header.operation.GetOperationStatusHeaders;
import org.apache.flink.table.gateway.rest.header.session.CloseSessionHeaders;
import org.apache.flink.table.gateway.rest.header.session.GetSessionConfigHeaders;
import org.apache.flink.table.gateway.rest.header.session.OpenSessionHeaders;
import org.apache.flink.table.gateway.rest.header.session.TriggerSessionHeartbeatHeaders;
import org.apache.flink.table.gateway.rest.header.statement.ExecuteStatementHeaders;
import org.apache.flink.table.gateway.rest.header.statement.FetchResultsHeaders;
import org.apache.flink.table.gateway.rest.message.operation.OperationHandleIdPathParameter;
import org.apache.flink.table.gateway.rest.message.operation.OperationMessageParameters;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.*;
import org.apache.flink.table.gateway.rest.message.statement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sessions")
@Api(value = "/sessions", tags = "Session接口")
public class SessionController {

    @Autowired
    private RestClient client;

    @PostMapping
    @ApiOperation("Opens a new session with specific properties. " +
            "Specific properties can be given for current session which will override the default properties of gateway.")
    public CompletableFuture<OpenSessionResponseBody> openSession() throws IOException {
        OpenSessionRequestBody body = new OpenSessionRequestBody(null, null);
        return client.sendRequest("localhost", 8083,
                OpenSessionHeaders.getInstance(),
                EmptyMessageParameters.getInstance(),
                body);
    }

    @GetMapping("{sessionHandle}")
    @ApiOperation("Get the session configuration.")
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(@PathVariable("sessionHandle") String sessionHandle) throws IOException, ConversionException {
        SessionMessageParameters parameters = new SessionMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                GetSessionConfigHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @PostMapping("{sessionHandle}/heartbeat")
    @ApiOperation("Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.")
    public CompletableFuture<EmptyResponseBody> heartbeat(@PathVariable("sessionHandle") String sessionHandle) throws IOException, ConversionException {
        SessionMessageParameters parameters = new SessionMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                TriggerSessionHeartbeatHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @DeleteMapping("{sessionHandle}")
    @ApiOperation("Closes the specific session.")
    public CompletableFuture<CloseSessionResponseBody> closeSession(@PathVariable("sessionHandle") String sessionHandle) throws IOException, ConversionException {
        SessionMessageParameters parameters = new SessionMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                CloseSessionHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @PostMapping("{sessionHandle}/statements")
    @ApiOperation("Execute a statement.")
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(@PathVariable("sessionHandle") String sessionHandle,
                                                                            @RequestBody ExecuteStatementParam param) throws IOException, ConversionException {
        SessionMessageParameters parameters = new SessionMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
        }
        ExecuteStatementRequestBody body = new ExecuteStatementRequestBody(param.getStatement(), param.getTimeout(), param.getExecutionConfig());
        return client.sendRequest("localhost", 8083,
                ExecuteStatementHeaders.getInstance(),
                parameters,
                body);
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/result/{token}")
    @ApiOperation("Fetch results of Operation.")
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle,
                                                                          @PathVariable("token") String token) throws IOException, ConversionException {
        FetchResultsTokenParameters parameters = new FetchResultsTokenParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
            if (pathParameter instanceof OperationHandleIdPathParameter) {
                pathParameter.resolveFromString(operationHandle);
            }
            if (pathParameter instanceof FetchResultsTokenPathParameter) {
                pathParameter.resolveFromString(token);
            }
        }
        return client.sendRequest("localhost", 8083,
                FetchResultsHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @GetMapping("{sessionHandle}/operations/{operationHandle}/status")
    @ApiOperation("Get the status of operation.")
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(@PathVariable("sessionHandle") String sessionHandle,
                                                                             @PathVariable("operationHandle") String operationHandle) throws IOException, ConversionException {
        OperationMessageParameters parameters = new OperationMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
            if (pathParameter instanceof OperationHandleIdPathParameter) {
                pathParameter.resolveFromString(operationHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                GetOperationStatusHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/cancel")
    @ApiOperation("Cancel the operation.")
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                          @PathVariable("operationHandle") String operationHandle) throws IOException, ConversionException {
        OperationMessageParameters parameters = new OperationMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
            if (pathParameter instanceof OperationHandleIdPathParameter) {
                pathParameter.resolveFromString(operationHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                CancelOperationHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }

    @PostMapping("{sessionHandle}/operations/{operationHandle}/close")
    @ApiOperation("Close the operation.")
    public CompletableFuture<OperationStatusResponseBody> closeOperation(@PathVariable("sessionHandle") String sessionHandle,
                                                                         @PathVariable("operationHandle") String operationHandle) throws IOException, ConversionException {
        OperationMessageParameters parameters = new OperationMessageParameters();
        Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
        for (MessagePathParameter pathParameter : pathParameters) {
            if (pathParameter instanceof SessionHandleIdPathParameter) {
                pathParameter.resolveFromString(sessionHandle);
            }
            if (pathParameter instanceof OperationHandleIdPathParameter) {
                pathParameter.resolveFromString(operationHandle);
            }
        }
        return client.sendRequest("localhost", 8083,
                CloseOperationHeaders.getInstance(),
                parameters,
                EmptyRequestBody.getInstance());
    }


}
