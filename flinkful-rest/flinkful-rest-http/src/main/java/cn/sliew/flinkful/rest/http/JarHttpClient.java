package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.JarClient;
import cn.sliew.flinkful.rest.http.util.FlinkShadedJacksonUtil;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.JobPlanInfo;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.util.RestConstants;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.webmonitor.handlers.*;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.flinkful.rest.http.FlinkHttpClient.APPLICATION_JSON;

public class JarHttpClient extends AsyncClient implements JarClient {

    private final String webInterfaceURL;

    public JarHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<JarListInfo> jars() throws IOException {
        String url = webInterfaceURL + "/jars";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JarListInfo.class);
    }

    @Override
    public CompletableFuture<JarUploadResponseBody> uploadJar(String filePath) throws IOException {
        String url = webInterfaceURL + "/jars/upload";
        File jarFile = new File(filePath);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("jarfile", jarFile.getName(), RequestBody.create(jarFile, MediaType.get(RestConstants.CONTENT_TYPE_JAR)))
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, JarUploadResponseBody.class);
    }

    @Override
    public CompletableFuture<EmptyResponseBody> deleteJar(String jarId) throws IOException {
        String url = webInterfaceURL + "/jars/" + jarId;
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        return remoteCall(request);
    }

    @Override
    public CompletableFuture<JobPlanInfo> jarPlan(String jarId, JarPlanRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/jars/" + jarId + "/plan";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, JobPlanInfo.class);
    }

    @Override
    public CompletableFuture<JarRunResponseBody> jarRun(String jarId, JarRunRequestBody requestBody) throws IOException {
        String url = webInterfaceURL + "/jars/" + jarId + "/run";
        RequestBody body = RequestBody.create(FlinkShadedJacksonUtil.toJsonString(requestBody), APPLICATION_JSON);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        return remoteCall(request, JarRunResponseBody.class);
    }
}
