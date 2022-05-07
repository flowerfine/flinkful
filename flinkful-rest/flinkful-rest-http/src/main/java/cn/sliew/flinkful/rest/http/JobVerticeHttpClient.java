package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.JobVerticeClient;
import cn.sliew.milky.common.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.*;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptDetailsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtasksAllAccumulatorsInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.webmonitor.threadinfo.JobVertexFlameGraph;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
    public CompletableFuture<JobVertexFlameGraph> jobVertexFlameGraph(String jobId, String vertexId, String type) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/flamegraph";
        if (StringUtils.isNotBlank(type)) {
            url = url + "?type=" + type;
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, JobVertexFlameGraph.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexMetrics(String jobId, String vertexId, String get) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/metrics";
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
    public CompletableFuture<SubtasksAllAccumulatorsInfo> jobVertexSubtaskAccumulators(String jobId, String vertexId) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/accumulators";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, SubtasksAllAccumulatorsInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> jobVertexSubtaskMetrics(String jobId, String vertexId, String get, String agg, String subtasks) throws IOException {
        String url = webInterfaceURL + "/jobs/" + jobId + "/vertices/" + vertexId + "/subtasks/metrics";
        List<String> queryParams = new LinkedList<>();
        if (StringUtils.isNotBlank(get)) {
            queryParams.add("get=" + get);
        }
        if (StringUtils.isNotBlank(agg)) {
            queryParams.add("agg=" + agg);
        }
        if (StringUtils.isNotBlank(subtasks)) {
            queryParams.add("subtasks=" + subtasks);
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
}
