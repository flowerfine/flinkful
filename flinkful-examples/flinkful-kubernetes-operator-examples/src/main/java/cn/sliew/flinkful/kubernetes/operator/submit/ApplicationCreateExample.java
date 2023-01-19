package cn.sliew.flinkful.kubernetes.operator.submit;

import cn.sliew.flinkful.kubernetes.operator.FlinkDeploymentBuilder;
import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.kubernetes.operator.api.FlinkDeployment;
import org.apache.flink.kubernetes.operator.api.spec.UpgradeMode;

public class ApplicationCreateExample {

    public static void main(String[] args) throws Exception {
        FlinkDeploymentBuilder builder = new FlinkDeploymentBuilder();
        builder
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata(ApplicationCreateExample::metadata)
                .spec(ApplicationCreateExample::spec);

        FlinkDeployment flinkDeployment = builder.getOrBuild();
        try (KubernetesClient kubernetesClient = new KubernetesClientBuilder()
                .withConfig(Config.autoConfigure("docker-desktop"))
                .build()) {
            FlinkDeployment orReplace =
                    kubernetesClient.resource(flinkDeployment).createOrReplace();
            System.out.println(Serialization.asYaml(orReplace));
        } catch (KubernetesClientException e) {
            e.printStackTrace();
        }
    }

    private static void metadata(ObjectMetaConfigurer objectMetaConfigurer) {
        objectMetaConfigurer.name("application-deployment222")
                .namespace("default");
    }

    private static void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .flinkConfiguration("state.checkpoints.dir", "file:///flink-data/checkpoints")
                .flinkConfiguration("state.savepoints.dir", "file:///flink-data/savepoints")
                .flinkConfiguration("taskmanager.numberOfTaskSlots", "2")
                .job()
                .jarURI("local:///opt/flink/examples/streaming/StateMachineExample.jar")
                .parallelism(2)
                .upgradeMode(UpgradeMode.STATELESS);
    }
}
