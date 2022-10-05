package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.base.util.Util;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterDeploymentException;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterSpecification;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClientProvider;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.YarnClusterDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.nio.file.Path;

@Slf4j
public class YarnSessionCreateCommand implements SessionCommand {

    @Override
    public ClusterClient create(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration) throws Exception {
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        Util.addJarFiles(clusterDescriptor, flinkHome, configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);
        return createClusterClient(clusterDescriptor, clusterSpecification);
    }

    private ClusterClient<ApplicationId> createClusterClient(YarnClusterDescriptor clusterDescriptor,
                                                             ClusterSpecification clusterSpecification) throws ClusterDeploymentException {

        ClusterClientProvider<ApplicationId> provider = clusterDescriptor.deploySessionCluster(clusterSpecification);
        ClusterClient<ApplicationId> clusterClient = provider.getClusterClient();

        log.info("deploy session with appId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
