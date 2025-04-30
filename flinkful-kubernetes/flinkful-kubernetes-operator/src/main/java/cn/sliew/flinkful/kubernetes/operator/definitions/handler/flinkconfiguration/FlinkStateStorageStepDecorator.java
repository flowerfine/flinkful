package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理状态存储，checkpoint, savepoint, ha
 */
@RequiredArgsConstructor
public class FlinkStateStorageStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    private final StorageConfigProperties storageConfigProperties;

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

        return flinkConfiguration;
    }
}
