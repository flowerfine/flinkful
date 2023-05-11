package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.DashboardClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.DashboardConfiguration;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DashboardHttpClient extends AsyncClient implements DashboardClient {

    private final String webInterfaceURL;

    public DashboardHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<DashboardConfiguration> config() throws IOException {
        String url = webInterfaceURL + "/config";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, DashboardConfiguration.class);
    }
}
