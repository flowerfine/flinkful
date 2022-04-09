package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.JobManagerClient;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.ClusterConfigurationInfo;
import org.apache.flink.runtime.rest.messages.ClusterConfigurationInfoHeaders;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.cluster.JobManagerLogListHeaders;
import org.apache.flink.runtime.rest.messages.job.metrics.JobManagerMetricsHeaders;
import org.apache.flink.runtime.rest.messages.job.metrics.JobManagerMetricsMessageParameters;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class JobManagerRestClient implements JobManagerClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public JobManagerRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<ClusterConfigurationInfo> jobmanagerConfig() throws IOException {
        return client.sendRequest(address, port, ClusterConfigurationInfoHeaders.getInstance());
    }

    @Override
    public CompletableFuture<LogListInfo> jobmanagerLogs() throws IOException {
        return client.sendRequest(address, port, JobManagerLogListHeaders.getInstance());
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobmanagerMetrics(Optional<String> get) throws IOException {
        JobManagerMetricsMessageParameters parameters = new JobManagerMetricsMessageParameters();
        get.ifPresent(metrics -> toIllegalArgument(() -> parameters.metricsFilterParameter.resolveFromString(metrics)));
        return client.sendRequest(address, port, JobManagerMetricsHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }
}
