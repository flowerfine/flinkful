package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import org.apache.flink.runtime.jobgraph.JobGraph;

public class KubernetesSessionSubmitCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<String> factory = createClientFactory(configuration);
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) factory.createClusterDescriptor(configuration);
        String clusterId = factory.getClusterId(configuration);
        ClusterClient<String> clusterClient = clusterDescriptor.retrieve(clusterId).getClusterClient();

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return clusterClient.submitJob(jobGraph).get();
    }

    private ClusterClientFactory<String> createClientFactory(Configuration config) {
        config.setString(KubernetesConfigOptions.CLUSTER_ID, "flink-cluster-7b367a19632fb03f4ff84a580e3d032");
        config.setString(DeploymentOptions.TARGET, KubernetesDeploymentTarget.SESSION.getName());

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }
}
