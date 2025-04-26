package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.v1.client.SqlGatewayClient;
import cn.sliew.flinkful.rest.base.v1.client.SqlProcessClient;
import cn.sliew.milky.common.exception.Rethrower;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.util.ConfigurationException;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqlGatewayRestClient implements SqlGatewayClient {

    private final String address;
    private final int port;
    private final RestClient client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ExecutorThreadFactory("Flink-RestClusterClient-IO"));

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public SqlGatewayRestClient(String address, int port, Configuration configuration) {
        this.address = address;
        this.port = port;

        RestClient restClient = null;
        try {
            restClient = new RestClient(configuration, executorService);
        } catch (ConfigurationException e) {
            Rethrower.throwAs(e);
        }
        this.client = restClient;
    }

    @Override
    public SqlProcessClient sqlProcess() {
        return (SqlProcessClient) cache.computeIfAbsent("sqlProcess", key -> new SqlProcessRestClient(address, port, client));
    }
}
