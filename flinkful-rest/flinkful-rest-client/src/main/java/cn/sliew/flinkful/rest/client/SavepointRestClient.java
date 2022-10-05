package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.SavepointClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.RestClient;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalRequest;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalStatusHeaders;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalStatusMessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalTriggerHeaders;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class SavepointRestClient implements SavepointClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public SavepointRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<TriggerResponse> savepointDisposal(SavepointDisposalRequest request) throws IOException {
        return client.sendRequest(address, port, SavepointDisposalTriggerHeaders.getInstance(), EmptyMessageParameters.getInstance(), request);
    }

    @Override
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> savepointDisposalResult(String triggerId) throws IOException {
        SavepointDisposalStatusMessageParameters parameters = new SavepointDisposalStatusMessageParameters();
        toIllegalArgument(() -> parameters.triggerIdPathParameter.resolveFromString(triggerId));
        return client.sendRequest(address, port, SavepointDisposalStatusHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }
}
