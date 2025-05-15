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

import cn.sliew.flinkful.rest.base.v1.client.ClusterClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.handler.legacy.messages.ClusterOverviewWithVersion;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ClusterHttpClient extends AsyncClient implements ClusterClient {

    private final String webInterfaceURL;

    public ClusterHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<ClusterOverviewWithVersion> overview() throws IOException {
        String url = webInterfaceURL + "/overview";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ClusterOverviewWithVersion.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> shutdownCluster() throws IOException {
        String url = webInterfaceURL + "/cluster";
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request);
    }
}
