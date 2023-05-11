package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.v1.client.ClusterClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.ClusterOverviewHeaders;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.cluster.ShutdownHeaders;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ClusterRestClient implements ClusterClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public ClusterRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        return client.sendRequest(address, port, ClusterOverviewHeaders.getInstance());
    }

    @Override
    public CompletableFuture<EmptyResponseBody> shutdownCluster() throws IOException {
        return client.sendRequest(address, port, ShutdownHeaders.getInstance());
    }

}
