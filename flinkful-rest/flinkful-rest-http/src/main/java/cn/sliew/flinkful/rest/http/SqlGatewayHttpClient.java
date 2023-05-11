package cn.sliew.flinkful.rest.http;

import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.base.v1.client.SqlProcessClient;
import okhttp3.OkHttpClient;
import org.apache.flink.runtime.rest.versioning.RuntimeRestAPIVersion;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SqlGatewayHttpClient implements SqlGatewayClient {

    private final String webInterfaceURL;
    private final OkHttpClient client;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public SqlGatewayHttpClient(String webInterfaceURL) {
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
