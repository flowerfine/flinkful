package cn.sliew.flinkful.kubernetes.operator.submit;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.apache.flink.kubernetes.operator.api.FlinkDeployment;
import org.apache.flink.kubernetes.operator.api.spec.*;

import java.util.Map;

import static java.util.Map.entry;

/**
 * client code for ../basic.yaml.
 */
public class Basic {
    public static void main(String[] args) {
        FlinkDeployment flinkDeployment = new FlinkDeployment();
        flinkDeployment.setApiVersion("flink.apache.org/v1beta1");
        flinkDeployment.setKind("FlinkDeployment");
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName("basic");
        objectMeta.setNamespace("default");
        flinkDeployment.setMetadata(objectMeta);
        FlinkDeploymentSpec flinkDeploymentSpec = new FlinkDeploymentSpec();
        flinkDeploymentSpec.setFlinkVersion(FlinkVersion.v1_16);
        flinkDeploymentSpec.setImage("flink:1.16");
        Map<String, String> flinkConfiguration =
                Map.ofEntries(entry("taskmanager.numberOfTaskSlots", "2"));
        flinkDeploymentSpec.setFlinkConfiguration(flinkConfiguration);
        flinkDeployment.setSpec(flinkDeploymentSpec);
        flinkDeploymentSpec.setServiceAccount("flink");
        JobManagerSpec jobManagerSpec = new JobManagerSpec();
        jobManagerSpec.setResource(new Resource(1.0, "2048m", null));
        flinkDeploymentSpec.setJobManager(jobManagerSpec);
        TaskManagerSpec taskManagerSpec = new TaskManagerSpec();
        taskManagerSpec.setResource(new Resource(1.0, "2048m", null));
        flinkDeploymentSpec.setTaskManager(taskManagerSpec);
        flinkDeployment
                .getSpec()
                .setJob(
                        JobSpec.builder()
                                .jarURI(
                                        "local:///opt/flink/examples/streaming/StateMachineExample.jar")
                                .parallelism(2)
                                .upgradeMode(UpgradeMode.STATELESS)
                                .build());

        try (KubernetesClient kubernetesClient = new KubernetesClientBuilder()
                .withConfig(Config.autoConfigure("docker-desktop"))
                .build()) {
            kubernetesClient.resource(flinkDeployment).createOrReplace();
        }
    }
}
