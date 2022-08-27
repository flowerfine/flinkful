package cn.sliew.flinkful.cli.frontend.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class NativeKubernetesApplicationExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        client.submitApplication(DeploymentTarget.NATIVE_KUBERNETES_APPLICATION, null, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 以 application 模式提交到 kubernetes 时，任务是以镜像的方式提交的。因此需要将任务 jar 和依赖打成镜像，进行提交。
     * kubernetes 提供了良好的资源申请机制，设置任务资源时可以避免设置过量的资源，而出现资源利用率低的问题。
     */
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
