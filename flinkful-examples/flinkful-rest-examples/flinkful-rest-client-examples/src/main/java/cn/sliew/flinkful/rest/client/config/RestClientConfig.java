/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
