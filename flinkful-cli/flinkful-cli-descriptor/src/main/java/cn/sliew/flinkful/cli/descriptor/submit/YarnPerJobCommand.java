package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.base.util.Util;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterDeploymentException;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterSpecification;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClientProvider;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgramUtils;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.CoreOptions;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.JobManagerOptions;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobgraph.JobGraph;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.util.HadoopUtils;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.YarnClusterDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.nio.file.Path;

@Slf4j
public class YarnPerJobCommand implements SubmitCommand {

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
    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        YarnClusterDescriptor clusterDescriptor = (YarnClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        Util.addJarFiles(clusterDescriptor, flinkHome, configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);

        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return createClusterClient(clusterDescriptor, clusterSpecification, jobGraph);
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
