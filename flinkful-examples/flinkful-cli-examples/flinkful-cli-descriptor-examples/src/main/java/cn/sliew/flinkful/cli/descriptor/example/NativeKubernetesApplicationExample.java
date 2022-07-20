package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

public class NativeKubernetesApplicationExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.15.1");
        client.submitApplication(DeploymentTarget.NATIVE_KUBERNETES_APPLICATION, flinkHome, buildConfiguration(), Util.buildJarJob());
    }

    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(1024).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(1024).getBytes());
        configuration.setString(KubernetesConfigOptions.CONTAINER_IMAGE, "jar-image-name");
        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
