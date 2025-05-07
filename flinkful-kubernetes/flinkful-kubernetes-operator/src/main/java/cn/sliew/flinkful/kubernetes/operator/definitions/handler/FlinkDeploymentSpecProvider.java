package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkDeploymentSpec;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface FlinkDeploymentSpecProvider {

    FlinkDeploymentSpec getSpec();

    default List<HasMetadata> getAdditionalResources() {
        return Collections.emptyList();
    }
}
