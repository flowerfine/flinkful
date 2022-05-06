package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.ConfigUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class NativeKubernetesSessionExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 在已经创建好的 flink-kubernetes-session 集群中提交任务，只需要设置 clusterId 即可。
     * 获取 clusterId 的方式有很多：
     *     1. 创建 flink-kubernetes-session 集群时可以获取 clusterId。
     *     2. clusterId 即为 flink-kubernetes-session 集群所在 pod 的 deployment id。可以从 kubernetes 中获取。
     */
    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(KubernetesConfigOptions.CLUSTER_ID, "flink-cluster-e40a0927cfe98fb3bf5c428a9583e60");
        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
