package cn.sliew.flinkful.rest.base;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface DataSetClient {

    /**
     * Returns all cluster data sets.
     */
    CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException;

    /**
     * Triggers the deletion of a cluster data set.
     * This async operation would return a 'triggerid' for further query identifier.
     *
     * @param datasetId - 32-character hexadecimal string value that identifies a cluster data set.
     */
    CompletableFuture<TriggerResponse> deleteDataSet(String datasetId) throws IOException;

    /**
     * Returns the status for the delete operation of a cluster data set.
     *
     * @param triggerId - 32-character hexadecimal string that identifies an asynchronous operation trigger ID. The ID was returned then the operation was triggered.
     */
    CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteDataSetStatus(String triggerId) throws IOException;

}
