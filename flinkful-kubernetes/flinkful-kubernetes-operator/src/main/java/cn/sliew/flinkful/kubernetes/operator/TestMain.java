package cn.sliew.flinkful.kubernetes.operator;

import cn.sliew.flinkful.kubernetes.operator.definitions.*;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.kubeclient.FlinkKubeClientFactory;
import org.apache.flink.kubernetes.shaded.io.fabric8.kubernetes.client.NamespacedKubernetesClient;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class TestMain {

    public static void main(String[] args) {
//        String yaml = testSessionCluster();
//        String yaml = testDeployment();
        String yaml = testStateSnapshot();
        System.out.println(yaml);

//        NamespacedKubernetesClient fabric8ioKubernetesClient = getKubernetesClient();
//        fabric8ioKubernetesClient
//                .load(new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)))
//                .createOrReplace();
    }

    private static String testSessionCluster() {
        DemoSessionClusterResourceDefinitionFactory sessionClusterResourceDefinitionFactory = new DemoSessionClusterResourceDefinitionFactory();
        SessionClusterResourceDefinition sessionClusterResourceDefinition = sessionClusterResourceDefinitionFactory.create();
        return Serialization.asYaml(sessionClusterResourceDefinition.getResource());
    }

    private static String testDeployment() {
        DemoDeploymentResourceDefinitionFactory demoDeploymentResourceDefinitionFactory = new DemoDeploymentResourceDefinitionFactory();
        DeploymentResourceDefinition deploymentResourceDefinition = demoDeploymentResourceDefinitionFactory.create();
        return Serialization.asYaml(deploymentResourceDefinition.getResource());
    }

    private static String testStateSnapshot() {
        DemoStateSnapshotResourceDefinitionFactory demoStateSnapshotResourceDefinitionFactory = new DemoStateSnapshotResourceDefinitionFactory();
        StateSnapshotResourceDefinition stateSnapshotResourceDefinition = demoStateSnapshotResourceDefinitionFactory.create();
        return Serialization.asYaml(stateSnapshotResourceDefinition.getResource());
    }

    private static NamespacedKubernetesClient getKubernetesClient() {
        FlinkKubeClientFactory flinkKubeClientFactory = FlinkKubeClientFactory.getInstance();
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        return flinkKubeClientFactory.createFabric8ioKubernetesClient(configuration);
    }
}
