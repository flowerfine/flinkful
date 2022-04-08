package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.*;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;

public class ClusterClientCommand implements Command {

    @Override
    public void submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<StandaloneClusterId> factory = createClientFactory(configuration);
        ClusterClient<StandaloneClusterId> client = createClusterClient(configuration, factory);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        JobID jobId = client.submitJob(jobGraph).get();
        System.out.println(jobId);
    }

    /**
     * Standalone 模式下可以使用 jobmanager 的地址或者使用 rest 地址。
     * 对于 yarn session 和 native kubernetes session 模式下，jobmanager 的地址由 yarn 或 native kubernetes 下处理，
     * 推荐使用 rest 地址。
     * todo jobmanager 地址 和 webInterfaceUrl 的优先级问题？
     */
    private ClusterClientFactory<StandaloneClusterId> createClientFactory(Configuration config) {
        config.setString(JobManagerOptions.ADDRESS, "localhost");
        config.setInteger(JobManagerOptions.PORT, 6123);
        config.setString(DeploymentOptions.TARGET, RemoteExecutor.NAME);

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    private ClusterClient<StandaloneClusterId> createClusterClient(Configuration configuration,
                                                                   ClusterClientFactory<StandaloneClusterId> factory) throws ClusterRetrieveException {
        StandaloneClusterId clusterId = factory.getClusterId(configuration);
        StandaloneClusterDescriptor clusterDescriptor = (StandaloneClusterDescriptor) factory.createClusterDescriptor(configuration);
        return clusterDescriptor.retrieve(clusterId).getClusterClient();
    }
}
