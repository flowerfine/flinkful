package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.ClusterClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "集群接口")
public class ClusterController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#overview-1
     */
    @GetMapping("overview")
    @Operation(summary = "集群概况", description = "集群概况")
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.overview();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/ops/rest_api/#cluster
     */
    @DeleteMapping
    @Operation(summary = "关闭集群", description = "关闭集群")
    public CompletableFuture<EmptyResponseBody> shutdown() throws IOException {
        ClusterClient clusterClient = restClient.cluster();
        return clusterClient.shutdownCluster();
    }
}
