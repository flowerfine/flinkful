package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.artifact.Artifact;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class DeploymentParameters extends FlinkResourceParameter {

    private final StorageConfigProperties properties;
    @Singular
    private final List<FileFetcherInitContainerStepDecorator.FileFetcherParam> fileFetcherParams;
    private Artifact artifact;
    @Builder.Default
    private int parallelism = 1;
}
