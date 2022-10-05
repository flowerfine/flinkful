package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterClientFactory;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgramUtils;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobgraph.JobGraph;

import java.nio.file.Path;

public class KubernetesSessionSubmitCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<String> factory = FlinkUtil.createClientFactory(configuration);
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) FlinkUtil.createClusterDescriptor(factory, configuration);

        String clusterId = factory.getClusterId(configuration);
        ClusterClient<String> clusterClient = clusterDescriptor.retrieve(clusterId).getClusterClient();

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        clusterClient.submitJob(jobGraph).get();
        return clusterClient;
    }
}
