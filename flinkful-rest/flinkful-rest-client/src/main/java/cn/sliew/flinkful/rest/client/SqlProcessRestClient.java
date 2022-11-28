package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.SqlProcessClient;
import cn.sliew.milky.common.exception.Rethrower;
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

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SqlProcessRestClient implements SqlProcessClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public SqlProcessRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<OpenSessionResponseBody> openSession(OpenSessionRequestBody requestBody) throws IOException {
        return client.sendRequest(address, port,
                OpenSessionHeaders.getInstance(),
                EmptyMessageParameters.getInstance(),
                requestBody);
    }

    @Override
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle) throws IOException {
        try {
            SessionMessageParameters parameters = new SessionMessageParameters();
            Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
            for (MessagePathParameter pathParameter : pathParameters) {
                if (pathParameter instanceof SessionHandleIdPathParameter) {
                    pathParameter.resolveFromString(sessionHandle);
                }
            }
            return client.sendRequest(address, port,
                    GetSessionConfigHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle) throws IOException {
        try {
            SessionMessageParameters parameters = new SessionMessageParameters();
            Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
            for (MessagePathParameter pathParameter : pathParameters) {
                if (pathParameter instanceof SessionHandleIdPathParameter) {
                    pathParameter.resolveFromString(sessionHandle);
                }
            }
            return client.sendRequest(address, port,
                    TriggerSessionHeartbeatHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle) throws IOException {
        try {
            SessionMessageParameters parameters = new SessionMessageParameters();
            Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
            for (MessagePathParameter pathParameter : pathParameters) {
                if (pathParameter instanceof SessionHandleIdPathParameter) {
                    pathParameter.resolveFromString(sessionHandle);
                }
            }
            return client.sendRequest(address, port,
                    CloseSessionHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle, ExecuteStatementRequestBody requestBody) throws IOException {
        try {
            SessionMessageParameters parameters = new SessionMessageParameters();
            Collection<MessagePathParameter<?>> pathParameters = parameters.getPathParameters();
            for (MessagePathParameter pathParameter : pathParameters) {
                if (pathParameter instanceof SessionHandleIdPathParameter) {
                    pathParameter.resolveFromString(sessionHandle);
                }
            }
            return client.sendRequest(address, port,
                    ExecuteStatementHeaders.getInstance(),
                    parameters,
                    requestBody);
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle, String operationHandle, String token) throws IOException {
        try {
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
            return client.sendRequest(address, port,
                    FetchResultsHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle, String operationHandle) throws IOException {
        try {
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
            return client.sendRequest(address, port,
                    GetOperationStatusHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle, String operationHandle) throws IOException {
        try {
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
            return client.sendRequest(address, port,
                    CancelOperationHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle, String operationHandle) throws IOException {
        try {
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
            return client.sendRequest(address, port,
                    CloseOperationHeaders.getInstance(),
                    parameters,
                    EmptyRequestBody.getInstance());
        } catch (ConversionException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
