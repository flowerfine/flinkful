package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.SqlGateWayClient;
import cn.sliew.flinkful.rest.base.SqlProcessClient;
import java.time.Duration;
import java.util.concurrent.ConcurrentMap;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.versioning.RuntimeRestAPIVersion;
import org.jboss.netty.util.internal.ConcurrentHashMap;


public class SqlGateWayHttpClient implements SqlGateWayClient {

    private final String webInterfaceURL;
    private final OkHttpClient client;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public SqlGateWayHttpClient(String webInterfaceURL) {
        this.webInterfaceURL =
            webInterfaceURL + "/" + RuntimeRestAPIVersion.V1.getURLVersionPrefix();
        this.client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(3L))
            .readTimeout(Duration.ofSeconds(3L))
            .writeTimeout(Duration.ofSeconds(3L))
            .callTimeout(Duration.ofSeconds(3L))
            .addInterceptor(new LogInterceptor())
            .build();
    }

    @Override
    public SqlProcessClient sqlProcess() {
        return (SqlProcessClient) cache
            .computeIfAbsent("sqlProcess",
                key -> new SqlProcessHttpClient(client, webInterfaceURL));
    }

}
