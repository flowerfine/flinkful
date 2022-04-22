package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import org.apache.flink.runtime.jobgraph.JobGraph;

/**
 * Native Kubernetes 部署需要利用 ${user.home}/.kube/config 信息获取 Kubernetes 信息
 */
@Slf4j
public class KubernetesSessionCreateCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<String> factory = createClientFactory(configuration);
        KubernetesClusterDescriptor clusterDescriptor = createClusterDescriptor(factory, configuration);
        ClusterSpecification clusterSpecification = YarnFlinkUtil.createClusterSpecification();
        ClusterClient<String> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification);

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return clusterClient.submitJob(jobGraph).get();
    }

    private ClusterClientFactory<String> createClientFactory(Configuration config) {
        config.setString(DeploymentOptions.TARGET, KubernetesDeploymentTarget.SESSION.getName());

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    private KubernetesClusterDescriptor createClusterDescriptor(ClusterClientFactory<String> factory,
                                                                Configuration configuration) {
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) factory.createClusterDescriptor(configuration);
        return clusterDescriptor;
    }

    private ClusterClient<String> createClusterClient(KubernetesClusterDescriptor clusterDescriptor,
                                                      ClusterSpecification clusterSpecification) throws ClusterDeploymentException {

        ClusterClientProvider<String> provider = clusterDescriptor.deploySessionCluster(clusterSpecification);
        ClusterClient<String> clusterClient = provider.getClusterClient();

        log.info("deploy session with clusterId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
