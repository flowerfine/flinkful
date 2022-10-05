package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.SavepointClient;
import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.flinkful.rest.http.FlinkHttpClient.APPLICATION_JSON;

public class SavepointHttpClient extends AsyncClient implements SavepointClient {

    private final String webInterfaceURL;

    public SavepointHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<TriggerResponse> savepointDisposal(SavepointDisposalRequest requestBody) throws IOException {
        String url = webInterfaceURL + "/savepoint-disposal";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, TriggerResponse.class);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> savepointDisposalResult(String triggerId) throws IOException {
        String url = webInterfaceURL + "/savepoint-disposal/" + triggerId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, AsynchronousOperationResult.class, AsynchronousOperationInfo.class).thenApply(result -> {
            AsynchronousOperationResult<AsynchronousOperationInfo> type = result;
            return type;
        });
    }
}
