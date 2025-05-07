package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.kubernetes.definition.ResourceParameter;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
public abstract class FlinkResourceParameter implements ResourceParameter {

    private final UUID id;
    private final String name;
    private final String namespace;
    private final String internalNamespace;

    private final StorageConfigProperties properties;
    @Singular
    private final List<FileFetcherInitContainerStepDecorator.FileFetcherParam> fileFetcherParams;
}
