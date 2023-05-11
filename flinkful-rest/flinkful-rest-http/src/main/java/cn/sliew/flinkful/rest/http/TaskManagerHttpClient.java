package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.TaskManagerClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.flink.runtime.rest.messages.LogListInfo;
import org.apache.flink.runtime.rest.messages.ThreadDumpInfo;
import org.apache.flink.runtime.rest.messages.job.metrics.AggregatedMetricsResponseBody;
import org.apache.flink.runtime.rest.messages.job.metrics.MetricCollectionResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TaskManagerHttpClient extends AsyncClient implements TaskManagerClient {

    private final String webInterfaceURL;

    public TaskManagerHttpClient(OkHttpClient client, String webInterfaceURL) {
        super(client);
        this.webInterfaceURL = webInterfaceURL;
    }

    @Override
    public CompletableFuture<TaskManagersInfo> taskManagers() throws IOException {
        String url = webInterfaceURL + "/taskmanagers";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, TaskManagersInfo.class);
    }

    @Override
    public CompletableFuture<AggregatedMetricsResponseBody> taskManagersMetrics(Optional<String> get, Optional<String> agg, Optional<String> taskmanagers) throws IOException {
        String url = webInterfaceURL + "/taskmanagers/metrics";
        List<String> queryParams = new LinkedList<>();
        if (get.isPresent()) {
            queryParams.add("get=" + get.get());
        }
        if (agg.isPresent()) {
            queryParams.add("agg=" + agg.get());
        }
        if (taskmanagers.isPresent()) {
            queryParams.add("taskmanagers=" + taskmanagers.get());
        }
        if (queryParams.isEmpty() == false) {
            String params = queryParams.stream().collect(Collectors.joining("&"));
            url = url + "?" + params;
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, AggregatedMetricsResponseBody.class);
    }

    @Override
    public CompletableFuture<TaskManagerDetailsInfo> taskManagerDetail(String taskManagerId) throws IOException {
        String url = webInterfaceURL + "/taskmanagers/" + taskManagerId;
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, TaskManagerDetailsInfo.class);
    }

    @Override
    public CompletableFuture<LogListInfo> taskManagerLogs(String taskManagerId) throws IOException {
        String url = webInterfaceURL + "/taskmanagers/" + taskManagerId + "/logs";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, LogListInfo.class);
    }

    @Override
    public CompletableFuture<MetricCollectionResponseBody> taskManagerMetrics(String taskManagerId, Optional<String> get) throws IOException {
        String url = webInterfaceURL + "/taskmanagers/" + taskManagerId + "/metrics";
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
    public CompletableFuture<ThreadDumpInfo> taskManagerThreadDump(String taskManagerId) throws IOException {
        String url = webInterfaceURL + "/taskmanagers/" + taskManagerId + "/thread-dump";
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return remoteCall(request, ThreadDumpInfo.class);
    }
}
