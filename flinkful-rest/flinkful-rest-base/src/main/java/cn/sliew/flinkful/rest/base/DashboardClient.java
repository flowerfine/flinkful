package cn.sliew.flinkful.rest.base;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.DashboardConfiguration;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface DashboardClient {

    /**
     * Returns the configuration of the WebUI.
     */
    CompletableFuture<DashboardConfiguration> config() throws IOException;
}
