package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SessionClusterParameters {

    // urls

    private StorageConfigProperties storageConfigProperties;

    private FlinkVersion flinkVersion;

}
