package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.PackageJarJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import org.apache.flink.runtime.client.JobStatusMessage;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 首先通过命令 docker build -f Dockerfile -t flink-example:1 . 创建镜像
 */
@Slf4j
public class KubernetesApplicationCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<String> factory = createClientFactory(configuration);
        KubernetesClusterDescriptor clusterDescriptor = createClusterDescriptor(factory, configuration, job);
        ClusterSpecification clusterSpecification = YarnFlinkUtil.createClusterSpecification();

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        ClusterClient<String> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        Optional<JobStatusMessage> optional = jobStatusMessages.stream().findFirst();
        if (optional.isEmpty()) {
            throw new IllegalStateException("任务信息异常");
        }
        return optional.get().getJobId();
    }

    private ClusterClientFactory<String> createClientFactory(Configuration config) {
        config.setString(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    private KubernetesClusterDescriptor createClusterDescriptor(ClusterClientFactory<String> factory, Configuration config, PackageJarJob job) {
        config.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(1024).getBytes());
        config.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(1024).getBytes());

        ConfigUtils.encodeCollectionToConfig(config, PipelineOptions.JARS, Collections.singletonList(new File(job.getJarFilePath())), Object::toString);
        config.setString(KubernetesConfigOptions.CONTAINER_IMAGE, buildImage());

        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) factory.createClusterDescriptor(config);
        return clusterDescriptor;
    }

    private String buildImage() {
        return "flink-example:1";
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
