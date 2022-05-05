package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

@Slf4j
public class YarnSessionSubmitCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<ApplicationId> factory = Util.createClientFactory(configuration);
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) Util.createClusterDescriptor(factory, configuration);

        ApplicationId clusterId = factory.getClusterId(configuration);
        ClusterClient<ApplicationId> clusterClient = clusterDescriptor.retrieve(clusterId).getClusterClient();

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return clusterClient.submitJob(jobGraph).get();
    }

}
