package cn.sliew.flinkful.kubernetes.operator.submit;

import cn.sliew.flinkful.kubernetes.operator.FlinkDeploymentBuilder;
import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.kubernetes.operator.api.FlinkDeployment;

public class SessionCreateExample {

    public static void main(String[] args) throws Exception {
        FlinkDeploymentBuilder config = new FlinkDeploymentBuilder();
        config
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata(SessionCreateExample::metadata)
                .spec(SessionCreateExample::spec);

        FlinkDeployment flinkDeployment = config.getOrBuild();
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
        objectMetaConfigurer.name("session-create-deployment");
    }

    private static void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .flinkConfiguration("taskmanager.numberOfTaskSlots", "2");
    }
}
