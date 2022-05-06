package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.ClusterClient;
import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cluster")
@Api(value = "/cluster", tags = "集群接口")
public class ClusterController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/ops/rest_api/#overview-1
     */
    @GetMapping("overview")
    @ApiOperation("集群概况")
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.overview();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/ops/rest_api/#cluster
     */
    @DeleteMapping
    @ApiOperation("关闭集群")
    public CompletableFuture<EmptyResponseBody> shutdown() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.shutdownCluster();
    }
}
