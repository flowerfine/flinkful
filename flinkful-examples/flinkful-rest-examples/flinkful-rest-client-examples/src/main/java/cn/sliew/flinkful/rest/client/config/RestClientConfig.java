package cn.sliew.flinkful.rest.client.config;

import cn.sliew.flinkful.examples.common.FlinkExamples;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    public RestClient restClient() {
        org.apache.flink.configuration.Configuration configuration = FlinkExamples.loadConfiguration();
        return new FlinkRestClient("localhost", 8081, configuration);
    }
}
