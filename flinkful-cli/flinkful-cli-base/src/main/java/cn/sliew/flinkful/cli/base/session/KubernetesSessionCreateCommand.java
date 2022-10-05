package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import lombok.extern.slf4j.Slf4j;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterDeploymentException;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterSpecification;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClientProvider;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.kubernetes.KubernetesClusterDescriptor;

import java.nio.file.Path;

/**
 * Native Kubernetes 部署需要利用 ${user.home}/.kube/config 信息获取 Kubernetes 信息
 */
@Slf4j
public class KubernetesSessionCreateCommand implements SessionCommand {

    @Override
    public ClusterClient create(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration) throws Exception {
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);
        return createClusterClient(clusterDescriptor, clusterSpecification);
    }

    private ClusterClient<String> createClusterClient(KubernetesClusterDescriptor clusterDescriptor,
                                                      ClusterSpecification clusterSpecification) throws ClusterDeploymentException {

        ClusterClientProvider<String> provider = clusterDescriptor.deploySessionCluster(clusterSpecification);
        ClusterClient<String> clusterClient = provider.getClusterClient();

        log.info("deploy session with clusterId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
