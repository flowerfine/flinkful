package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.JobManagerClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.ClusterConfigurationInfo;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class JobManagerHttpClient extends AsyncClient implements JobManagerClient {

    private final String webInterfaceURL;

    public JobManagerHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<ClusterConfigurationInfo> jobmanagerConfig() throws IOException {
        String url = webInterfaceURL + "/jobmanager/config";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ClusterConfigurationInfo.class);
    }

    @Override
    public CompletableFuture<LogListInfo> jobmanagerLogs() throws IOException {
        String url = webInterfaceURL + "/jobmanager/logs";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, LogListInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobmanagerMetrics(Optional<String> get) throws IOException {
        String url = webInterfaceURL + "/jobmanager/metrics";
        if (get.isPresent()) {
            url = url + "?get=" + get.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }
}
