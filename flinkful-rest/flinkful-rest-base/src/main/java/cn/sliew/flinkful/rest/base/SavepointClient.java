package cn.sliew.flinkful.rest.base;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface SavepointClient {

    /**
     * Triggers the desposal of a savepoint. This async operation would return a 'triggerid' for further query identifier.
     *
     * @param request
     */
    CompletableFuture<TriggerResponse> savepointDisposal(SavepointDisposalRequest request) throws IOException;

    /**
     * Returns the status of a savepoint disposal operation.
     *
     * @param triggerId 32-character hexadecimal string that identifies an asynchronous operation trigger ID. The ID was returned then the operation was triggered.
     */
    CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> savepointDisposalResult(String triggerId) throws IOException;

}
