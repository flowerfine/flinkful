package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.yarn.configuration.YarnConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class YarnPerJobExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.13.6");
        client.submit(DeploymentTarget.YARN_PER_JOB, flinkHome, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 与 YARN application 模式类似。参考 {@link YarnApplicationExample#buildConfiguration()}
     */
    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(ConfigConstants.PATH_HADOOP_CONFIG, "/var/folders/q4/lbzr8_ds6cl7ml690ldq00zc0000gn/T/8929050144804066824/local_docker_kubernetes");
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(4096).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(4096).getBytes());
        configuration.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);

        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://namenode:9002/flink/1.13.6"}));
        configuration.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://namenode:9002/flink/1.13.6/flink-dist_2.11-1.13.6.jar");

        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
