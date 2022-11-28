package cn.sliew.flinkful.rest.base;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.CloseSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.session.GetSessionConfigResponseBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionRequestBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementRequestBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.FetchResultsResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface SqlProcessClient {

    CompletableFuture<OpenSessionResponseBody> openSession(OpenSessionRequestBody requestBody) throws IOException;

    CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle) throws IOException;

    CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle) throws IOException;

    CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle) throws IOException;

    CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle, ExecuteStatementRequestBody requestBody) throws IOException;

    CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle, String operationHandle, String token) throws IOException;

    CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle, String operationHandle) throws IOException;

    CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle, String operationHandle) throws IOException;

    CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle,
                                                                  String operationHandle) throws IOException, ConversionException;

}
