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
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
public class YarnApplicationCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<ApplicationId> factory = createClientFactory(configuration);
        YarnClusterDescriptor clusterDescriptor = createClusterDescriptor(factory, configuration, job);
        ClusterSpecification clusterSpecification = YarnFlinkUtil.createClusterSpecification();

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        ClusterClient<ApplicationId> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification, applicationConfiguration);
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        Optional<JobStatusMessage> optional = jobStatusMessages.stream().findFirst();
        if (optional.isEmpty()) {
            throw new IllegalStateException("任务信息异常");
        }
        return optional.get().getJobId();
    }

    private ClusterClientFactory<ApplicationId> createClientFactory(Configuration config) {
        config.setString(DeploymentOptions.TARGET, YarnDeploymentTarget.APPLICATION.getName());

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    private YarnClusterDescriptor createClusterDescriptor(ClusterClientFactory<ApplicationId> factory, Configuration config, PackageJarJob job) throws MalformedURLException {
        config.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(4096).getBytes());
        config.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(4096).getBytes());
        config.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);

        config.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://hadoop:9000/flink/1.13.6"}));
        config.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://hadoop:9000/flink/1.13.6/flink-dist_2.11-1.13.6.jar");
//        Util.addJarFiles(clusterDescriptor, config);

        ConfigUtils.encodeCollectionToConfig(config, PipelineOptions.JARS, Collections.singletonList(new File(job.getJarFilePath())), Object::toString);

        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) factory.createClusterDescriptor(config);
        return clusterDescriptor;
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
