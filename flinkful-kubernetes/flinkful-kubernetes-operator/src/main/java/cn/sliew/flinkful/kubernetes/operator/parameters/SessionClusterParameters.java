package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.Logging;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class SessionClusterParameters extends FlinkResourceParameter {

    private final FlinkVersion flinkVersion;
    private final StorageConfigProperties properties;
    @Singular
    private final List<FileFetcherInitContainerStepDecorator.FileFetcherParam> fileFetcherParams;
    private final Logging logging;
}
