package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.runtime.client.JobStatusMessage;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

/**
 * 首先通过命令 docker build -f Dockerfile -t flink-example:1 . 创建镜像
 */
@Slf4j
public class KubernetesApplicationCommand implements SubmitCommand {

    @Override
    public JobID submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        ClusterClient<String> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        Optional<JobStatusMessage> optional = jobStatusMessages.stream().findFirst();
        if (optional.isPresent() == false) {
            throw new IllegalStateException("任务信息异常");
        }
        return optional.get().getJobId();
    }

    private ClusterClient<String> createClusterClient(KubernetesClusterDescriptor clusterDescriptor,
                                                      ClusterSpecification clusterSpecification,
                                                      ApplicationConfiguration applicationConfiguration) throws ClusterDeploymentException {

        ClusterClientProvider<String> provider = clusterDescriptor.deployApplicationCluster(clusterSpecification, applicationConfiguration);
        ClusterClient<String> clusterClient = provider.getClusterClient();
        log.info("deploy application with clusterId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
