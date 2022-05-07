package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.util.RestConstants;
import org.apache.flink.runtime.rest.versioning.RestAPIVersion;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;

public class FlinkHttpClient implements RestClient {

    public static final MediaType APPLICATION_JSON = MediaType.get(RestConstants.REST_CONTENT_TYPE);

    private final String webInterfaceURL;
    private final OkHttpClient client;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public FlinkHttpClient(String webInterfaceURL) {
        this.webInterfaceURL = webInterfaceURL + "/" + RestAPIVersion.V1.getURLVersionPrefix();
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
