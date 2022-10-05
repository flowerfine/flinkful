package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterDeploymentException;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterSpecification;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.application.ApplicationConfiguration;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClientProvider;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * 首先通过命令 docker build -f Dockerfile -t flink-example:1 . 创建镜像
 */
@Slf4j
public class KubernetesApplicationCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        return createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
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
