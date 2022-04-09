package cn.sliew.flinkful.rest.base;

import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface ClusterClient {

    /**
     * Returns an overview over the Flink cluster.
     */
    CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException;

    /**
     * Shuts down the cluster
     */
    CompletableFuture<EmptyResponseBody> shutdownCluster() throws IOException;
}
