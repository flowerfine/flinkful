package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class SessionClusterParameters {

    private UUID id;
    private FlinkVersion flinkVersion;
    @Singular
    private List<FileFetcherInitContainerStepDecorator.FileFetcherParam> fileFetcherParams;
    private StorageConfigProperties properties;
    private Map<String, String> labels;
}
