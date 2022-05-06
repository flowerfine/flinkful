package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public class YarnApplicationCommand implements SubmitCommand {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        Util.addJarFiles(clusterDescriptor, configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        ClusterClient<ApplicationId> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        Optional<JobStatusMessage> optional = jobStatusMessages.stream().findFirst();
        if (optional.isPresent() == false) {
            throw new IllegalStateException("任务信息异常");
        }
        return optional.get().getJobId();
    }

    private ClusterClient<ApplicationId> createClusterClient(YarnClusterDescriptor clusterDescriptor,
                                                             ClusterSpecification clusterSpecification,
                                                             ApplicationConfiguration applicationConfiguration) throws ClusterDeploymentException {

        ClusterClientProvider<ApplicationId> provider = clusterDescriptor.deployApplicationCluster(clusterSpecification, applicationConfiguration);
        ClusterClient<ApplicationId> clusterClient = provider.getClusterClient();
        log.info("deploy application with appId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
