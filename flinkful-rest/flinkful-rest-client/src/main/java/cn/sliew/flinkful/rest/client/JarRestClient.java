package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.v1.client.JarClient;
import org.apache.flink.runtime.rest.FileUpload;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.JobPlanInfo;
import org.apache.flink.runtime.rest.util.RestConstants;
import org.apache.flink.runtime.webmonitor.handlers.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.exception.Rethrower.toIllegalArgument;

public class JarRestClient implements JarClient {

    private final String address;
    private final int port;
    private final RestClient client;

    public JarRestClient(String address, int port, RestClient client) {
        this.address = address;
        this.port = port;
        this.client = client;
    }

    @Override
    public CompletableFuture<JarListInfo> jars() throws IOException {
        return client.sendRequest(address, port, JarListHeaders.getInstance());
    }

    @Override
    public CompletableFuture<JarUploadResponseBody> uploadJar(String filePath) throws IOException {
        FileUpload upload = new FileUpload(Paths.get(filePath), RestConstants.CONTENT_TYPE_BINARY);
        return client.sendRequest(address, port, JarUploadHeaders.getInstance(), EmptyMessageParameters.getInstance(), EmptyRequestBody.getInstance(), Collections.singleton(upload));
    }

    @Override
    public CompletableFuture<EmptyResponseBody> deleteJar(String jarId) throws IOException {
        JarDeleteMessageParameters parameters = new JarDeleteMessageParameters();
        toIllegalArgument(() -> parameters.jarIdPathParameter.resolveFromString(jarId));
        return client.sendRequest(address, port, JarDeleteHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
    }

    @Override
    public CompletableFuture<JobPlanInfo> jarPlan(String jarId, JarPlanRequestBody requestBody) throws IOException {
        JarPlanMessageParameters parameters = new JarPlanMessageParameters();
        toIllegalArgument(() -> parameters.jarIdPathParameter.resolveFromString(jarId));
        return client.sendRequest(address, port, JarPlanGetHeaders.getInstance(), parameters, requestBody);
    }

    @Override
    public CompletableFuture<JarRunResponseBody> jarRun(String jarId, JarRunRequestBody requestBody) throws IOException {
        JarRunMessageParameters parameters = new JarRunMessageParameters();
        toIllegalArgument(() -> parameters.jarIdPathParameter.resolveFromString(jarId));
        return client.sendRequest(address, port, JarRunHeaders.getInstance(), parameters, requestBody);
    }
}
