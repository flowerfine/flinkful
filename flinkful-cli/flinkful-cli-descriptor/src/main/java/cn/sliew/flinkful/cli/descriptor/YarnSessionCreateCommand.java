package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

@Slf4j
public class YarnSessionCreateCommand implements Command {

    /**
     * 1. 创建 session 集群
     * 2. 提交任务
     * 3. 使用已有的 session 集群既可以通过 ApplicationId 也可以通过 JobManager 的地址
     *
     * @see ClusterClientCommand
     * @see RestClusterClientCommand
     * @see YarnSessionSubmitCommand
     */
    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) Util.createClusterDescriptor(configuration);
        ClusterSpecification clusterSpecification = Util.createClusterSpecification(configuration);

        ClusterClient<ApplicationId> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return clusterClient.submitJob(jobGraph).get();
    }

    private ClusterClient<ApplicationId> createClusterClient(YarnClusterDescriptor clusterDescriptor,
                                                             ClusterSpecification clusterSpecification) throws ClusterDeploymentException {

        ClusterClientProvider<ApplicationId> provider = clusterDescriptor.deploySessionCluster(clusterSpecification);
        ClusterClient<ApplicationId> clusterClient = provider.getClusterClient();

        log.info("deploy session with appId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
