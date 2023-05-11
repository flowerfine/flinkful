package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.ClusterClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ClusterHttpClient extends AsyncClient implements ClusterClient {

    private final String webInterfaceURL;

    public ClusterHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        String url = webInterfaceURL + "/overview";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ClusterOverviewWithVersion.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> shutdownCluster() throws IOException {
        String url = webInterfaceURL + "/cluster";
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request);
    }
}
