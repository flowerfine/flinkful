package cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkStateSnapshotSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.FlinkStateSnapshotStatus;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkStateSnapshot {

    private final String kind = ResourceKinds.STATE_SNAPSHOT;
    private final String apiVersion = ResourceVersions.FLINK_VERSION;
    private final FlinkStateSnapshotMetadata metadata;
    private final FlinkStateSnapshotSpec spec;
    private final FlinkStateSnapshotStatus status;

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FlinkStateSnapshotMetadata {

        private final String id;
        private final String name;
        private final String namespace;
        private final Map<String, String> labels;
        private final Map<String, String> annotations;
        private final Integer resourceVersion;
    }
}
