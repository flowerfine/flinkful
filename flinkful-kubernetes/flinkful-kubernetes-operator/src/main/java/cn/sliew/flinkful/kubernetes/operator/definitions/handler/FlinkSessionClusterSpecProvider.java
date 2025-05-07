package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkSessionClusterSpec;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface FlinkSessionClusterSpecProvider {

    FlinkSessionClusterSpec getSpec();

    default List<HasMetadata> getAdditionalResources() {
        return Collections.emptyList();
    }
}
