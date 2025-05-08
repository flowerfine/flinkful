package cn.sliew.flinkful.kubernetes.operator.entity.filefetcher;

import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ImmutableList;
import io.fabric8.kubernetes.api.model.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFetcher {

    private final String kind = ResourceKinds.FILE_FETCHER;
    private final String apiVersion = ResourceVersions.FLINKFUL_VERSION;
    private final FileFetcherMetadata metadata;
    private final FileFetcherSpec spec;

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FileFetcherMetadata {

        private final UUID id;
        private final String name;
        private final String namespace;
        private final Map<String, String> labels;
        private final Map<String, String> annotations;
    }

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FileFetcherSpec {

        private final ContainerImage image;
        @Builder.Default
        private final List<Volume> volumes = Collections.emptyList();
        @Builder.Default
        private final List<VolumeMount> volumeMounts = Collections.emptyList();
        @Builder.Default
        private final List<EnvVar> envVars = Collections.emptyList();
        @Builder.Default
        private final List<String> args = Collections.emptyList();
        @Builder.Default
        private ResourceRequirements resources = new ResourceRequirements();
    }
}
