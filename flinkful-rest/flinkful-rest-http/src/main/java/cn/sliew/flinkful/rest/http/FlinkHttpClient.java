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

import cn.sliew.flinkful.rest.base.v1.client.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.util.RestConstants;
import org.apache.flink.runtime.rest.versioning.RuntimeRestAPIVersion;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FlinkHttpClient implements RestClient {

    public static final MediaType APPLICATION_JSON = MediaType.get(RestConstants.REST_CONTENT_TYPE);

    private final String webInterfaceURL;
    private final OkHttpClient client;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public FlinkHttpClient(String webInterfaceURL) {
        this.webInterfaceURL = webInterfaceURL + "/" + RuntimeRestAPIVersion.V1.getURLVersionPrefix();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(3L))
                .readTimeout(Duration.ofSeconds(3L))
                .writeTimeout(Duration.ofSeconds(3L))
                .callTimeout(Duration.ofSeconds(3L))
                .addInterceptor(new LogInterceptor())
                .build();
    }

    @Override
    public ClusterClient cluster() {
        return (ClusterClient) cache.computeIfAbsent("cluster", key -> new ClusterHttpClient(client, webInterfaceURL));
    }

    @Override
    public DataSetClient dataSet() {
        return (DataSetClient) cache.computeIfAbsent("dataSet", key -> new DataSetHttpClient(client, webInterfaceURL));
    }

    @Override
    public JarClient jar() {
        return (JarClient) cache.computeIfAbsent("jar", key -> new JarHttpClient(client, webInterfaceURL));
    }

    @Override
    public JobClient job() {
        return (JobClient) cache.computeIfAbsent("job", key -> new JobHttpClient(client, webInterfaceURL));
    }

    @Override
    public JobVerticeClient jobVertice() {
        return (JobVerticeClient) cache.computeIfAbsent("jobVertice", key -> new JobVerticeHttpClient(client, webInterfaceURL));
    }

    @Override
    public JobManagerClient jobManager() {
        return (JobManagerClient) cache.computeIfAbsent("jobManager", key -> new JobManagerHttpClient(client, webInterfaceURL));
    }

    @Override
    public TaskManagerClient taskManager() {
        return (TaskManagerClient) cache.computeIfAbsent("taskManager", key -> new TaskManagerHttpClient(client, webInterfaceURL));
    }

    @Override
    public SavepointClient savepoint() {
        return (SavepointClient) cache.computeIfAbsent("savepoint", key -> new SavepointHttpClient(client, webInterfaceURL));
    }

    @Override
    public DashboardClient dashboard() {
        return (DashboardClient) cache.computeIfAbsent("dashboard", key -> new DashboardHttpClient(client, webInterfaceURL));
    }
}
