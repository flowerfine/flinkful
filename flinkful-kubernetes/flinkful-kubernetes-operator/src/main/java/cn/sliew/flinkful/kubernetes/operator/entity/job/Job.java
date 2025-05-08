package cn.sliew.flinkful.kubernetes.operator.entity.job;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.JobStatus;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
public final class Job {

    private final String kind = ResourceKinds.JOB;
    private final String apiVersion = ResourceVersions.FLINK_VERSION;
    private final JobMetadata metadata;
    private final JobSpec spec;
    private final JobStatus status;

    @JsonIgnore
    public Boolean isSessionMode() {
        return Objects.nonNull(metadata) && Objects.nonNull(metadata.getSessionClusterId());
    }

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class JobMetadata {

        private final UUID id;
        private final String name;
        private final String namespace;

        private final UUID deploymentId;
        private final String deploymentName;
        private final UUID sessionClusterId;
        private final String sessionClusterName;

        private final Map<String, String> labels;
        private final Map<String, String> annotations;
    }
}
