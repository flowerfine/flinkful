package cn.sliew.flinkful.kubernetes.operator.submit;

import org.apache.flink.kubernetes.operator.api.FlinkDeployment;
import org.apache.flink.kubernetes.operator.api.spec.FlinkDeploymentSpec;
import org.apache.flink.kubernetes.operator.api.spec.FlinkVersion;
import org.apache.flink.kubernetes.operator.api.spec.JobManagerSpec;
import org.apache.flink.kubernetes.operator.api.spec.JobSpec;
import org.apache.flink.kubernetes.operator.api.spec.Resource;
import org.apache.flink.kubernetes.operator.api.spec.TaskManagerSpec;
import org.apache.flink.kubernetes.operator.api.spec.UpgradeMode;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.Map;

import static java.util.Map.entry;

/** client code for ../basic.yaml. */
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
        jobManagerSpec.setResource(new Resource(1.0, "2048m"));
        flinkDeploymentSpec.setJobManager(jobManagerSpec);
        TaskManagerSpec taskManagerSpec = new TaskManagerSpec();
        taskManagerSpec.setResource(new Resource(1.0, "2048m"));
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

        try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().build()) {
            kubernetesClient.resource(flinkDeployment).createOrReplace();
        }
    }
}
