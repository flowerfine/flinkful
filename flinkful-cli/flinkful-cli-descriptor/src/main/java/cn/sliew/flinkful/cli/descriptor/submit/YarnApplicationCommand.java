package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.base.util.Util;
import lombok.extern.slf4j.Slf4j;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterDeploymentException;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterSpecification;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.application.ApplicationConfiguration;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClientProvider;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.nio.file.Path;

@Slf4j
public class YarnApplicationCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        Util.addJarFiles(clusterDescriptor, flinkHome, configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        return createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
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
