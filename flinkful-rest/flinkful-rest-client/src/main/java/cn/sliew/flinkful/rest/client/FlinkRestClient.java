package cn.sliew.flinkful.rest.client;

import cn.sliew.flinkful.rest.base.*;
import cn.sliew.milky.common.exception.Rethrower;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.util.ConfigurationException;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlinkRestClient implements cn.sliew.flinkful.rest.base.RestClient {

    private final String address;
    private final int port;
    private final RestClient client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ExecutorThreadFactory("Flink-RestClusterClient-IO"));

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(8);

    public FlinkRestClient(String address, int port, Configuration configuration) {
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
    public ClusterClient cluster() {
        return (ClusterClient) cache.computeIfAbsent("cluster", key -> new ClusterRestClient(address, port, client));
    }

    @Override
    public DataSetClient dataSet() {
        return (DataSetClient) cache.computeIfAbsent("dataSet", key -> new DataSetRestClient(address, port, client));
    }

    @Override
    public JarClient jar() {
        return (JarClient) cache.computeIfAbsent("jar", key -> new JarRestClient(address, port, client));
    }

    @Override
    public JobClient job() {
        return (JobClient) cache.computeIfAbsent("job", key -> new JobRestClient(address, port, client));
    }

    @Override
    public JobVerticeClient jobVertice() {
        return (JobVerticeClient) cache.computeIfAbsent("jobVertice", key -> new JobVerticeRestClient(address, port, client));
    }

    @Override
    public JobManagerClient jobManager() {
        return (JobManagerClient) cache.computeIfAbsent("jobManager", key -> new JobManagerRestClient(address, port, client));
    }

    @Override
    public TaskManagerClient taskManager() {
        return (TaskManagerClient) cache.computeIfAbsent("taskManager", key -> new TaskManagerRestClient(address, port, client));
    }

    @Override
    public SavepointClient savepoint() {
        return (SavepointClient) cache.computeIfAbsent("savepoint", key -> new SavepointRestClient(address, port, client));
    }

    @Override
    public DashboardClient dashboard() {
        return (DashboardClient) cache.computeIfAbsent("dashboard", key -> new DashboardRestClient(address, port, client));
    }
}
