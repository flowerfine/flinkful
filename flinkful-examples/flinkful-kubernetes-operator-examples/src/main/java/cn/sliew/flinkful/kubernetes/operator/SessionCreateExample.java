package cn.sliew.flinkful.kubernetes.operator;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;

public class SessionCreateExample {

    public static void main(String[] args) throws Exception {
        FlinkDeployment flinkDeployment = Util.build();
        try (KubernetesClient kubernetesClient = new DefaultKubernetesClient()) {
            FlinkDeployment orReplace =
                    kubernetesClient.resource(flinkDeployment).createOrReplace();
            System.out.println(Serialization.asYaml(orReplace));
        } catch (KubernetesClientException e) {
            // some error while connecting to kube cluster
            e.printStackTrace();
        }
    }
}
