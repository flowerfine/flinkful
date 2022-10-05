package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.DataSetClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DataSetHttpClient extends AsyncClient implements DataSetClient {

    private final String webInterfaceURL;

    public DataSetHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException {
        String url = webInterfaceURL + "/datasets";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ClusterDataSetListResponseBody.class);
    }

    @Override
    public CompletableFuture<TriggerResponse> deleteDataSet(String datasetId) throws IOException {
        String url = webInterfaceURL + "/datasets/" + datasetId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, TriggerResponse.class);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteDataSetStatus(String triggerId) throws IOException {
        String url = webInterfaceURL + "/datasets/delete/" + triggerId;
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
