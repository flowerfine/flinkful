package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.DataSetClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.RestClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.dataset.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class DataSetRestClient implements DataSetClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public DataSetRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException {
        return client.sendRequest(address, port, ClusterDataSetListHeaders.INSTANCE);
    }

    @Override
    public CompletableFuture<TriggerResponse> deleteDataSet(String datasetId) throws IOException {
        ClusterDataSetDeleteTriggerMessageParameters parameters = new ClusterDataSetDeleteTriggerMessageParameters();
        toIllegalArgument(() -> parameters.clusterDataSetIdPathParameter.resolveFromString(datasetId));
        return client.sendRequest(address, port, ClusterDataSetDeleteTriggerHeaders.INSTANCE, parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteDataSetStatus(String triggerId) throws IOException {
        ClusterDataSetDeleteStatusMessageParameters parameters = new ClusterDataSetDeleteStatusMessageParameters();
        toIllegalArgument(() -> parameters.triggerIdPathParameter.resolveFromString(triggerId));
        return client.sendRequest(address, port, ClusterDataSetDeleteStatusHeaders.INSTANCE, parameters, EmptyRequestBody.getInstance());
    }
}
