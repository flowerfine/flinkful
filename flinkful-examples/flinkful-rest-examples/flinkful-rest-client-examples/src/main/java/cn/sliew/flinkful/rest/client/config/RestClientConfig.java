package cn.sliew.flinkful.rest.client.config;

import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.flinkful.rest.client.SqlGatewayRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        org.apache.flink.configuration.Configuration configuration = FlinkExamples
                .loadConfiguration();
        return new FlinkRestClient("localhost", 8081, configuration);
    }

    @Bean
    public SqlGatewayClient sqlGateWayRestClient() {
        org.apache.flink.configuration.Configuration configuration = FlinkExamples
                .loadConfiguration();
        return new SqlGatewayRestClient("localhost", 8083, configuration);
    }
}
