package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.DataSetClient;
import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.dataset.ClusterDataSetListResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
