package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import cn.sliew.flinkful.kubernetes.operator.parameters.SessionClusterParameters;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoSessionClusterResourceDefinitionFactory implements SessionClusterResourceDefinitionFactory {

    private static final UUID DEFAULT_SESSION_CLUSTER_ID = UUID.fromString("dac4ca57-6dd2-3168-3d27-7e23a91c85d7");
    private static final String DEFAULT_SESSION_CLUSTER_NAME = "test-session-cluster";

    private final SessionClusterParameters parameters;

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
        Map<String, String> labels = Map.of("system", "flinkful",
                "internalNamespace", "default",
                "app", "flink",
                "instance", "instance-1",
                "component", "session-cluster",
                "sessionClusterId", DEFAULT_SESSION_CLUSTER_ID.toString(),
                "sessionClusterName", DEFAULT_SESSION_CLUSTER_NAME);

        return () -> {
            return SessionCluster.SessionClusterMetadata.builder()
                    .id(DEFAULT_SESSION_CLUSTER_ID)
                    .name(DEFAULT_SESSION_CLUSTER_NAME)
                    .namespace("default")
                    .labels(parameters.getLabels())
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkSessionClusterSpecProvider getFlinkSessionClusterSpecProvider() {
//        SessionClusterParameters parameters = SessionClusterParameters.builder()
//                .sessionClusterId(DEFAULT_SESSION_CLUSTER_ID)
//                .flinkVersion(FlinkVersion.V_1_18_1)
//                .properties(properties)
//                .build();
        return new DefaultFlinkSessionClusterSpecProvider(parameters);
    }
}
