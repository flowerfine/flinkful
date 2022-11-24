package cn.sliew.flinkful.rest.client.config;

import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.base.SqlGateWayClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.flinkful.rest.http.SqlGateWayHttpClient;
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
    public SqlGateWayClient sqlGateWayRestClient() {
        org.apache.flink.configuration.Configuration configuration = FlinkExamples
            .loadConfiguration();
        return new SqlGateWayHttpClient("http://localhost:8083");
    }
}
