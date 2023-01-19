package cn.sliew.flinkful.kubernetes.operator.submit;

import cn.sliew.flinkful.kubernetes.operator.FlinkDeploymentBuilder;
import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
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
        Config config = Config.autoConfigure("docker-desktop");
        config.setRequestTimeout(1000 * 60 * 60);
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient(config)) {
            FlinkDeployment orReplace =
                    kubernetesClient.resource(flinkDeployment).createOrReplace();
            System.out.println(Serialization.asYaml(orReplace));
        } catch (KubernetesClientException e) {
            e.printStackTrace();
        }
    }

    private static void metadata(ObjectMetaConfigurer objectMetaConfigurer) {
        objectMetaConfigurer.name("application-deployment")
                .namespace("default");
    }

    private static void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .flinkConfiguration("taskmanager.numberOfTaskSlots", "2")
                .job()
                .jarURI("local:///opt/flink/examples/streaming/StateMachineExample.jar")
                .parallelism(2)
                .upgradeMode(UpgradeMode.STATELESS);
    }
}
