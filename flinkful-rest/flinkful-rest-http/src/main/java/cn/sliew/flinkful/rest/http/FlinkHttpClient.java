package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.util.RestConstants;
import org.apache.flink.runtime.rest.versioning.RestAPIVersion;

import java.time.Duration;

public class FlinkHttpClient implements RestClient {

    public static final MediaType APPLICATION_JSON = MediaType.get(RestConstants.REST_CONTENT_TYPE);

    private final String webInterfaceURL;
    private final OkHttpClient client;

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
        return new ClusterHttpClient(client, webInterfaceURL);
    }

    @Override
    public DataSetClient dataSet() {
        return new DataSetHttpClient(client, webInterfaceURL);
    }

    @Override
    public JarClient jar() {
        return new JarHttpClient(client, webInterfaceURL);
    }

    @Override
    public JobClient job() {
        return new JobHttpClient(client, webInterfaceURL);
    }

    @Override
    public JobManagerClient jobManager() {
        return new JobManagerHttpClient(client, webInterfaceURL);
    }

    @Override
    public TaskManagerClient taskManager() {
        return new TaskManagerHttpClient(client, webInterfaceURL);
    }

    @Override
    public SavepointClient savepoint() {
        return new SavepointHttpClient(client, webInterfaceURL);
    }

    @Override
    public DashboardClient dashboard() {
        return new DashboardHttpClient(client, webInterfaceURL);
    }
}
