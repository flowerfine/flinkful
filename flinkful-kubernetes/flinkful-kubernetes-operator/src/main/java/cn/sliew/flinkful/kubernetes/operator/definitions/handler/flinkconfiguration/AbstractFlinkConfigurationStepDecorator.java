package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import io.fabric8.kubernetes.api.model.HasMetadata;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractFlinkConfigurationStepDecorator implements FlinkConfigurationStepDecorator {

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        return parameters;
    }

    @Override
    public List<HasMetadata> buildRelatedResources() throws IOException {
        return Collections.emptyList();
    }
}
