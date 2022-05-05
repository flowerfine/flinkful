package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.runtime.jobgraph.JobGraph;

public class KubernetesSessionSubmitCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<String> factory = Util.createClientFactory(configuration);
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) Util.createClusterDescriptor(factory, configuration);
        
        String clusterId = factory.getClusterId(configuration);
        ClusterClient<String> clusterClient = clusterDescriptor.retrieve(clusterId).getClusterClient();

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return clusterClient.submitJob(jobGraph).get();
    }
}
