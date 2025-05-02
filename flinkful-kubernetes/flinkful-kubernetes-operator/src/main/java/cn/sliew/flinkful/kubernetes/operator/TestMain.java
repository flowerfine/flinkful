package cn.sliew.flinkful.kubernetes.operator;

import cn.sliew.flinkful.kubernetes.operator.definitions.DemoSessionClusterResourceDefinitionFactory;
import cn.sliew.flinkful.kubernetes.operator.definitions.SessionClusterResourceDefinition;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.kubeclient.FlinkKubeClientFactory;
import org.apache.flink.kubernetes.shaded.io.fabric8.kubernetes.client.NamespacedKubernetesClient;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class TestMain {

    public static void main(String[] args) {
        DemoSessionClusterResourceDefinitionFactory sessionClusterResourceDefinitionFactory = new DemoSessionClusterResourceDefinitionFactory();
        SessionClusterResourceDefinition sessionClusterResourceDefinition = sessionClusterResourceDefinitionFactory.create();

        FlinkKubeClientFactory flinkKubeClientFactory = FlinkKubeClientFactory.getInstance();
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        NamespacedKubernetesClient fabric8ioKubernetesClient = flinkKubeClientFactory.createFabric8ioKubernetesClient(configuration);

        String yaml = Serialization.asYaml(sessionClusterResourceDefinition.getSessionCluster());

        System.out.println(yaml);

        fabric8ioKubernetesClient
                .load(new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)))
                .createOrReplace();
    }
}
