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
package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.base.v1.client.SqlProcessClient;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.versioning.RuntimeRestAPIVersion;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SqlGatewayHttpClient implements SqlGatewayClient {

    private final String webInterfaceURL;
    private final OkHttpClient client;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public SqlGatewayHttpClient(String webInterfaceURL) {
        this.webInterfaceURL =
                webInterfaceURL + "/" + RuntimeRestAPIVersion.V1.getURLVersionPrefix();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(3L))
                .readTimeout(Duration.ofSeconds(3L))
                .writeTimeout(Duration.ofSeconds(3L))
                .callTimeout(Duration.ofSeconds(3L))
                .addInterceptor(new LogInterceptor())
                .build();
    }

    @Override
    public SqlProcessClient sqlProcess() {
        return (SqlProcessClient) cache
                .computeIfAbsent("sqlProcess",
                        key -> new SqlProcessHttpClient(client, webInterfaceURL));
    }

}
