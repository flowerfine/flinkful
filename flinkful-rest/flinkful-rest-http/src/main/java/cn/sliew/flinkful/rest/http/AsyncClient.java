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

import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import cn.sliew.milky.common.exception.Rethrower;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class AsyncClient {

    private final OkHttpClient client;

    public AsyncClient(OkHttpClient client) {
        this.client = client;
    }

    protected CompletableFuture<EmptyResponseBody> remoteCall(Request request) throws IOException {
        FutureResponse future = new FutureResponse();
        client.newCall(request).enqueue(future);
        return map(future, json -> EmptyResponseBody.getInstance());
    }

    protected <Out, In> CompletableFuture<Out> remoteCall(Request request, Class<Out> outClass, Class<In> parameterClasses) throws IOException {
        FutureResponse future = new FutureResponse();
        client.newCall(request).enqueue(future);
        return map(future, json -> FlinkShadedJacksonUtil.parseJsonString(json, outClass, parameterClasses));
    }

    protected <T> CompletableFuture<T> remoteCall(Request request, Class<T> responseClass) throws IOException {
        FutureResponse future = new FutureResponse();
        client.newCall(request).enqueue(future);
        return map(future, json -> FlinkShadedJacksonUtil.parseJsonString(json, responseClass));
    }

    private <T> CompletableFuture<T> map(FutureResponse future, Function<String, T> parser) throws IOException {
        return future.future.thenApply(response -> {
            try {
                checkStatus(response);
                return parser.apply(response.body().string());
            } catch (IOException e) {
                Rethrower.throwAs(e);
                return null;
            } finally {
                response.close();
            }
        });
    }

    private void checkStatus(Response response) throws IOException {
        if (response.isSuccessful() == false) {
            String error = String.format("code: %d, message: %s, body: %s", response.code(), response.message(), response.body().string());
            throw new RuntimeException(error);
        }
    }
}
