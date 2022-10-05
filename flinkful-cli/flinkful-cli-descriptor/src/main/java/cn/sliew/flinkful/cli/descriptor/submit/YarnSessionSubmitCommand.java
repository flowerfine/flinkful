package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import lombok.extern.slf4j.Slf4j;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterClientFactory;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgramUtils;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobgraph.JobGraph;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.nio.file.Path;

@Slf4j
public class YarnSessionSubmitCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<ApplicationId> factory = FlinkUtil.createClientFactory(configuration);
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) FlinkUtil.createClusterDescriptor(factory, configuration);

        ApplicationId clusterId = factory.getClusterId(configuration);
        ClusterClient<ApplicationId> clusterClient = clusterDescriptor.retrieve(clusterId).getClusterClient();

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        clusterClient.submitJob(jobGraph).get();
        return clusterClient;
    }

}
