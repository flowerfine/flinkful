package cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkSessionClusterSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.FlinkDeploymentStatus;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.UUID;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SessionCluster {

    private final String kind = ResourceKinds.SESSION_CLUSTER;
    private final String apiVersion = ResourceVersions.DEFAULT_VERSION;
    private final SessionClusterMetadata metadata;
    private final FlinkSessionClusterSpec spec;
    private final FlinkDeploymentStatus status;

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class SessionClusterMetadata {
        private final String name;
        private final String namespace;
        private final Map<String, String> labels;
        private final Map<String, String> annotations;
    }

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class SessionClusterMetadataLabels {

        private final String system;
        private final String internalNamespace;
        private final String app;
        private final String instance;
        private final String component;

        private final String sessionClusterId;
        private final String sessionClusterName;
    }
}
