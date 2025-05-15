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

import cn.sliew.flinkful.rest.base.v1.client.JobManagerClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.ConfigurationInfo;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class JobManagerHttpClient extends AsyncClient implements JobManagerClient {

    private final String webInterfaceURL;

    public JobManagerHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<ConfigurationInfo> jobmanagerConfig() throws IOException {
        String url = webInterfaceURL + "/jobmanager/config";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ConfigurationInfo.class);
    }

    @Override
    public CompletableFuture<LogListInfo> jobmanagerLogs() throws IOException {
        String url = webInterfaceURL + "/jobmanager/logs";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, LogListInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobmanagerMetrics(Optional<String> get) throws IOException {
        String url = webInterfaceURL + "/jobmanager/metrics";
        if (get.isPresent()) {
            url = url + "?get=" + get.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }

    @Override
    public CompletableFuture<ThreadDumpInfo> jobmanagerThreadDump() throws IOException {
        String url = webInterfaceURL + "/jobmanager/thread-dump";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ThreadDumpInfo.class);
    }
}
