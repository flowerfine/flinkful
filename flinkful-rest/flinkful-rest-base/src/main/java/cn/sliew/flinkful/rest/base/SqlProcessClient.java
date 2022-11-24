package cn.sliew.flinkful.rest.base;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.CloseSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.session.GetSessionConfigResponseBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementRequestBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.FetchResultsResponseBody;

public interface SqlProcessClient {

    CompletableFuture<OpenSessionResponseBody> openSession() throws IOException;

    CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle)
        throws IOException, ConversionException;

    CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle)
        throws IOException, ConversionException;

    CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle)
        throws IOException, ConversionException;

    CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle,
        ExecuteStatementRequestBody requestBody) throws IOException, ConversionException;

    CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle,
        String operationHandle,
        String token) throws IOException, ConversionException;

    CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle,
        String operationHandle) throws IOException, ConversionException;

    CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle,
        String operationHandle) throws IOException, ConversionException;

    CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle,
        String operationHandle) throws IOException, ConversionException;

}
