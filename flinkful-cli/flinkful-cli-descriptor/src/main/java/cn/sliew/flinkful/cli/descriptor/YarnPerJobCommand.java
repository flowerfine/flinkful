package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
public class YarnPerJobCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<ApplicationId> factory = createClientFactory(configuration);
        YarnClusterDescriptor clusterDescriptor = createClusterDescriptor(factory, configuration);
        ClusterSpecification clusterSpecification = YarnFlinkUtil.createClusterSpecification();
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        ClusterClient<ApplicationId> clusterClient = createClusterClient(clusterDescriptor, clusterSpecification, jobGraph);
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        Optional<JobStatusMessage> optional = jobStatusMessages.stream().findFirst();
        if (optional.isPresent() == false) {
            throw new IllegalStateException("任务信息异常");
        }
        return optional.get().getJobId();
    }

    /**
     * 需提供 hadoop 的配置文件，以便 flink 获取 hadoop 集群地址。
     * 当使用 flink on yarn 配置时，不需要配置 {@link JobManagerOptions#ADDRESS} 参数，
     * 配置 hadoop 配置文件，可以通过 {@link CoreOptions#FLINK_YARN_CONF_DIR} 和 {@link CoreOptions#FLINK_HADOOP_CONF_DIR}，
     * {@link CoreOptions#FLINK_YARN_CONF_DIR} 拥有更高的优先级，当二者都未配置时，flink 会尝试从 HADOOP_HOME 环境变量
     * 获取 hadoop 配置。
     * {@link CoreOptions#FLINK_YARN_CONF_DIR} 和 {@link CoreOptions#FLINK_HADOOP_CONF_DIR} 只支持环境变量形式设置，
     * 设置两个参数的目的仅仅是为了文档的自动生成.
     *
     * @see HadoopUtils#getHadoopConfiguration(Configuration)
     */
    private ClusterClientFactory<ApplicationId> createClientFactory(Configuration config) {
//        config.setString(JobManagerOptions.ADDRESS, "localhost");
//        config.setString(CoreOptions.FLINK_YARN_CONF_DIR, HADOOP_CONF_DIR);
//        config.setString(CoreOptions.FLINK_HADOOP_CONF_DIR, HADOOP_CONF_DIR);
        config.setString(DeploymentOptions.TARGET, YarnDeploymentTarget.PER_JOB.getName());

        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    /**
     * 也可以通过 {@link YarnConfigOptions#FLINK_DIST_JAR} 配置 flink-dist-xxx.jar
     * {@link YarnConfigOptions#SHIP_FILES} 配置 ship jars.
     */
    private YarnClusterDescriptor createClusterDescriptor(ClusterClientFactory<ApplicationId> factory, Configuration config) throws MalformedURLException {
        config.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        config.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        config.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);

        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) factory.createClusterDescriptor(config);
        YarnFlinkUtil.addJarFiles(clusterDescriptor, config);
        return clusterDescriptor;
    }

    private ClusterClient<ApplicationId> createClusterClient(YarnClusterDescriptor clusterDescriptor,
                                                                    ClusterSpecification clusterSpecification,
                                                                    JobGraph jobGraph) throws ClusterDeploymentException {

        ClusterClientProvider<ApplicationId> provider = clusterDescriptor.deployJobCluster(clusterSpecification, jobGraph, true);
        ClusterClient<ApplicationId> clusterClient = provider.getClusterClient();
        log.info("deploy per_job with appId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
