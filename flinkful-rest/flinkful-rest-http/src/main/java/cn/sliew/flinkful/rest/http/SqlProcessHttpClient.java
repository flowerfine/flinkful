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

import cn.sliew.flinkful.rest.base.v1.client.SqlProcessClient;
import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.table.gateway.rest.message.operation.OperationStatusResponseBody;
import org.apache.flink.table.gateway.rest.message.session.CloseSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.session.GetSessionConfigResponseBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionRequestBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.*;
import org.apache.flink.table.gateway.rest.message.util.GetApiVersionResponseBody;
import org.apache.flink.table.gateway.rest.message.util.GetInfoResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.flinkful.rest.http.FlinkHttpClient.APPLICATION_JSON;

public class SqlProcessHttpClient extends AsyncClient implements
        SqlProcessClient {

    private final String webInterfaceURL;

    public SqlProcessHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<GetApiVersionResponseBody> getApiVersion() throws IOException {
        String url = webInterfaceURL + "/api_versions";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, GetApiVersionResponseBody.class);
    }

    @Override
    public CompletableFuture<GetInfoResponseBody> getInfo() throws IOException {
        String url = webInterfaceURL + "/info";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, GetInfoResponseBody.class);
    }

    @Override
    public CompletableFuture<OpenSessionResponseBody> openSession(OpenSessionRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/sessions";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, OpenSessionResponseBody.class);
    }

    @Override
    public CompletableFuture<GetSessionConfigResponseBody> getSessionConfig(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, GetSessionConfigResponseBody.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> heartbeat(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/heartbeat";
        Request request = new Request.Builder()
                .url(url)
                .build();
        return remoteCall(request, EmptyResponseBody.class);
    }

    @Override
    public CompletableFuture<CloseSessionResponseBody> closeSession(String sessionHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle;
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request, CloseSessionResponseBody.class);
    }

    @Override
    public CompletableFuture<CompleteStatementResponseBody> completeStatement(String sessionHandle, CompleteStatementRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/statements";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, CompleteStatementResponseBody.class);
    }

    @Override
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(String sessionHandle, ExecuteStatementRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/statements";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, ExecuteStatementResponseBody.class);
    }

    @Override
    public CompletableFuture<FetchResultsResponseBody> getStatementResult(String sessionHandle, String operationHandle, String token, String rowFormat) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/result/" + token + "?rowFormat=" + rowFormat;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, FetchResultsResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> getOperationStatus(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/status";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> cancelOperation(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/cancel";
        Request request = new Request.Builder()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }

    @Override
    public CompletableFuture<OperationStatusResponseBody> closeOperation(String sessionHandle, String operationHandle) throws IOException {
        String url = webInterfaceURL + "/sessions/" + sessionHandle + "/operations/" + operationHandle + "/close";
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request, OperationStatusResponseBody.class);
    }
}
