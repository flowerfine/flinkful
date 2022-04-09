package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.DashboardClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.DashboardConfiguration;
import org.apache.flink.runtime.rest.messages.DashboardConfigurationHeaders;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DashboardRestClient implements DashboardClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public DashboardRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<DashboardConfiguration> config() throws IOException {
        return client.sendRequest(address, port, DashboardConfigurationHeaders.getInstance());
    }
}
