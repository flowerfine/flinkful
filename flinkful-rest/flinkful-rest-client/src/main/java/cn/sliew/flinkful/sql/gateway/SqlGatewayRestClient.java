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
package cn.sliew.flinkful.sql.gateway;

import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.base.v1.client.SqlProcessClient;
import cn.sliew.milky.common.exception.Rethrower;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.util.ConfigurationException;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqlGatewayRestClient implements SqlGatewayClient {

    private final String address;
    private final int port;
    private final RestClient client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ExecutorThreadFactory("Flink-RestClusterClient-IO"));

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public SqlGatewayRestClient(String address, int port, Configuration configuration) {
        this.address = address;
        this.port = port;

        RestClient restClient = null;
        try {
            restClient = new RestClient(configuration, executorService);
        } catch (ConfigurationException e) {
            Rethrower.throwAs(e);
        }
        this.client = restClient;
    }

    @Override
    public SqlProcessClient sqlProcess() {
        return (SqlProcessClient) cache.computeIfAbsent("sqlProcess", key -> new SqlProcessRestClient(address, port, client));
    }
}
