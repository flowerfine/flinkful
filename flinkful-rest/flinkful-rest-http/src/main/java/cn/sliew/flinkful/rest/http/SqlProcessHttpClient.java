package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.SqlProcessClient;
import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

import static cn.sliew.flinkful.rest.http.FlinkHttpClient.APPLICATION_JSON;

public class SqlProcessHttpClient extends AsyncClient implements
        SqlProcessClient {

    private final String webInterfaceURL;

    public SqlProcessHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<OpenSessionResponseBody> openSession(OpenSessionRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/sessions";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, OpenSessionResponseBody.class);
    }

    @Override
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, GetSessionConfigResponseBody.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/heartbeat";
        Request request = new Request.Builder()
                .url(url)
                .build();
        return remoteCall(request, EmptyResponseBody.class);
    }

    @Override
    public CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle;
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request, CloseSessionResponseBody.class);
    }

    @Override
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle, ExecuteStatementRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/statements";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, ExecuteStatementResponseBody.class);
    }

    @Override
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle, String operationHandle, String token) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/result/" + token;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, FetchResultsResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/status";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/cancel";
        Request request = new Request.Builder()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/close";
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }
}
