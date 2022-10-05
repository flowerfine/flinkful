package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.ConfigUtils;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.PipelineOptions;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.configuration.YarnConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class YarnSessionExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.13.6");
        client.submit(DeploymentTarget.YARN_SESSION, flinkHome, buildConfiguration(), Util.buildJarJob());
    }

    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(YarnConfigOptions.APPLICATION_ID, "application_1651836467749_0017");

        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://hadoop:9000/flink/1.13.6"}));
        configuration.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://hadoop:9000/flink/1.13.6/flink-dist_2.11-1.13.6.jar");

        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
