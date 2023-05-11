package cn.sliew.flinkful.rest.http.v1;

import cn.sliew.flinkful.rest.base.v1.messages.async.AsynchronousOperationResult;
import cn.sliew.flinkful.rest.base.v1.messages.async.TriggerResponse;
import cn.sliew.flinkful.rest.base.v1.messages.dataset.ClusterDataSetList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;

public interface DataSetClientV1 {

    @RequestLine("GET v1/datasets")
    @Headers("Content-Type: application/json")
    ClusterDataSetList datasets();

    @RequestLine("GET v1/datasets/{datasetId}")
    @Headers("Content-Type: application/json")
    TriggerResponse deleteDataSet(@Param("datasetId") String datasetId);

    @RequestLine("GET v1/datasets/delete/{triggerId}")
    @Headers("Content-Type: application/json")
    AsynchronousOperationResult<AsynchronousOperationInfo> deleteDataSetStatus(@Param("triggerId") String triggerId);
}
