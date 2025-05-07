package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DefaultDeploymentResourceDefinition implements DeploymentResourceDefinition {

    private final Deployment resource;
    private final List<HasMetadata> additionalResources;
}
