package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.JobVerticeClient;
import cn.sliew.milky.common.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptDetailsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtasksAllAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.webmonitor.threadinfo.VertexFlameGraph;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class JobVerticeHttpClient extends AsyncClient implements JobVerticeClient {

    private final String webInterfaceURL;

    public JobVerticeHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<JobVertexDetailsInfo> jobVertexDetail(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobVertexDetailsInfo.class);
    }

    @Override
    public CompletableFuture<JobVertexAccumulatorsInfo> jobVertexAccumulators(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/accumulators";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobVertexAccumulatorsInfo.class);
    }

    @Override
    public CompletableFuture<JobVertexBackPressureInfo> jobVertexBackPressure(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/backpressure";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobVertexBackPressureInfo.class);
    }

    @Override
    public CompletableFuture<VertexFlameGraph> jobVertexFlameGraph(String jobId, String vertexId, Optional<String> type) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/flamegraph";
        if (type.isPresent()) {
            url = url + "?type=" + type.get();
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, VertexFlameGraph.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexMetrics(String jobId, String vertexId, Optional<String> get) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/metrics";
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
    public CompletableFuture<SubtasksTimesInfo> jobVertexSubtaskTimes(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasktimes";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtasksTimesInfo.class);
    }

    @Override
    public CompletableFuture<JobVertexTaskManagersInfo> jobVertexTaskManagers(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/taskmanagers";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobVertexTaskManagersInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexWatermarks(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/watermarks";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }

    @Override
    public CompletableFuture<SubtasksAllAccumulatorsInfo> jobVertexSubtaskAccumulators(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/accumulators";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtasksAllAccumulatorsInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Optional<String> get, Optional<String> agg, Optional<String> subtasks) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/metrics";
        List<String> queryParams = new LinkedList<>();
        if (get.isPresent()) {
            queryParams.add("get=" + get.get());
        }
        if (agg.isPresent()) {
            queryParams.add("agg=" + agg.get());
        }
        if (subtasks.isPresent()) {
            queryParams.add("subtasks=" + subtasks.get());
        }
        if (queryParams.isEmpty() == false) {
            String params = queryParams.stream().collect(Collectors.joining("&"));
            url = url + "?" + params;
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskDetail(String jobId, String vertexId, Integer subtaskindex) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/" + subtaskindex;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtaskExecutionAttemptDetailsInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, Integer subtaskindex, String get) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/" + subtaskindex + "/metrics";
        if (StringUtils.isNotBlank(get)) {
            url = url + "?get=" + get;
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, MetricCollectionResponseBody.class);
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptDetailsInfo> jobVertexSubtaskAttemptDetail(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/" + subtaskindex + "/attempts/" + attempt;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtaskExecutionAttemptDetailsInfo.class);
    }

    @Override
    public CompletableFuture<SubtaskExecutionAttemptAccumulatorsInfo> jobVertexSubtaskAttemptAccumulators(String jobId, String vertexId, Integer subtaskindex, Integer attempt) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/" + subtaskindex + "/attempts/" + attempt + "/accumulators";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtaskExecutionAttemptAccumulatorsInfo.class);
    }

}
