package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.common.dict.k8s.CarpK8sImagePullPolicy;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.*;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import cn.sliew.flinkful.kubernetes.operator.util.FlinkConfigurations;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class DemoSessionClusterResourceDefinitionFactory implements SessionClusterResourceDefinitionFactory {

    private static final UUID DEFAULT_SESSION_CLUSTER_ID = UUID.fromString("dac4ca57-6dd2-3168-3d27-7e23a91c85d7");
    private static final String DEFAULT_SESSION_CLUSTER_NAME = "test";

    @Override
    public SessionClusterResourceDefinition create() {
        return new DefaultSessionClusterResourceDefinition(createSessionCluster());
    }

    private SessionCluster createSessionCluster() {
        return SessionCluster.builder()
                .metadata(getFlinkSessionClusterMetadataProvider().getMetadata())
                .spec(getFlinkSessionClusterSpecProvider().getSpec())
                .build();
    }

    private FlinkSessionClusterMetadataProvider getFlinkSessionClusterMetadataProvider() {
        return () -> {
            return SessionCluster.SessionClusterMetadata.builder()
                    .id(DEFAULT_SESSION_CLUSTER_ID)
                    .name(DEFAULT_SESSION_CLUSTER_NAME)
                    .namespace("default")
                    .labels(Map.of("system", "flinkful",
                            "internalNamespace", "default",
                            "app", "flink",
                            "instance", "instance-1",
                            "component", "session-cluster",
                            "sessionClusterId", DEFAULT_SESSION_CLUSTER_ID.toString(),
                            "sessionClusterName", DEFAULT_SESSION_CLUSTER_NAME))
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkSessionClusterSpecProvider getFlinkSessionClusterSpecProvider() {
        Map<String, String> flinkConfiguration = FlinkConfigurations.createFlinkConfiguration();
        flinkConfiguration.putAll(FlinkConfigurations.createSessionClusterConfiguration());

        return () -> {
            return FlinkSessionClusterSpec.builder()
                    .imagePullPolicy(CarpK8sImagePullPolicy.IF_NOT_PRESENT.getValue())
                    .image("flink:1.18")
                    .flinkVersion(OperatorFlinkVersion.v1_18)
                    .serviceAccount("flink")
                    .jobManager(JobManagerSpec.builder()
                            .resource(new Resource(1.0, "1G", null))
                            .replicas(1)
                            .build())
                    .taskManager(TaskManagerSpec.builder()
                            .resource(new Resource(1.0, "1G", null))
                            .replicas(1)
                            .build())
                    .logConfiguration(Map.of(
                            "cn.sliew", "DEBUG"
                    ))
                    .flinkConfiguration(flinkConfiguration)
                    .mode(KubernetesDeploymentMode.NATIVE)
                    .build();
        };
    }
}
