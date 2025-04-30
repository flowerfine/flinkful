package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import io.fabric8.kubernetes.api.model.HasMetadata;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FlinkConfigurationStepDecorator {

    Map<String, String> decorate(Map<String, String> parameters);

    List<HasMetadata> buildRelatedResources() throws IOException;
}
