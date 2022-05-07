package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.DataSetClient;
import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/datasets")
@Api(value = "/datasets", tags = "DataSet接口")
public class DataSetController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/ops/rest_api/#datasets
     */
    @GetMapping
    @ApiOperation("data set 列表")
    public CompletableFuture<ClusterDataSetListResponseBody> datasets() throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.datasets();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#datasets-datasetid
     */
    @DeleteMapping("/{dataSetId}")
    @ApiOperation("异步删除 data set")
    public CompletableFuture<TriggerResponse> delete(@PathVariable("dataSetId") String dataSetId) throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.deleteDataSet(dataSetId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#datasets-delete-triggerid
     */
    @GetMapping("/delete/{triggerId}")
    @ApiOperation("异步删除 data set 结果")
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> deleteStatus(@PathVariable("triggerId") String triggerId) throws IOException {
        DataSetClient dataSetClient = restClient.dataSet();
        return dataSetClient.deleteDataSetStatus(triggerId);
    }
}
