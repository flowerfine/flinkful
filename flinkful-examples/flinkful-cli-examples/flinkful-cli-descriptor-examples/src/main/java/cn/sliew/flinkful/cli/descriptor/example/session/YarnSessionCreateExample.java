package cn.sliew.flinkful.cli.descriptor.example.session;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.SessionClient;
import cn.sliew.flinkful.cli.descriptor.example.Util;
import cn.sliew.flinkful.cli.descriptor.example.YarnApplicationExample;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.*;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class YarnSessionCreateExample {

    public static void main(String[] args) throws Exception {
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.13.6");
        ClusterClient<ApplicationId> clusterClient = SessionClient.create(DeploymentTarget.YARN_SESSION, flinkHome, buildSessionConfiguration());
        CliClient client = Util.buildCliClient();
        client.submit(DeploymentTarget.YARN_SESSION, flinkHome, buildConfiguration(clusterClient.getClusterId()), Util.buildJarJob());
    }

    private static Configuration buildSessionConfiguration() {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.set(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        return configuration;
    }

    /**
     * 创建 YARN session 集群的过程与 YARN application 模式类似。
     * 参考 {@link YarnApplicationExample#buildConfiguration()}
     */
    private static Configuration buildConfiguration(ApplicationId clusterId) throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(YarnConfigOptions.APPLICATION_ID, clusterId.toString());

        // 使用
        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://hadoop:9000/flink/1.13.6"}));
        configuration.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://hadoop:9000/flink/1.13.6/flink-dist_2.11-1.13.6.jar");

        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
