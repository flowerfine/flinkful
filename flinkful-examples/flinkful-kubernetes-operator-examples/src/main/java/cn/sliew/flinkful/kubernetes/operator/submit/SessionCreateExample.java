package cn.sliew.flinkful.kubernetes.operator.submit;

import cn.sliew.flinkful.kubernetes.operator.FlinkDeploymentBuilder;
import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;

public class SessionCreateExample {

    public static void main(String[] args) throws Exception {
        FlinkDeploymentBuilder config = new FlinkDeploymentBuilder();
        config
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata(SessionCreateExample::metadata)
                .spec(SessionCreateExample::spec);

        FlinkDeployment flinkDeployment = config.getOrBuild();
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
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
