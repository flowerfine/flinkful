package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.kubernetes.definition.CustomResourceDefinitionFactory;
import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;

@FunctionalInterface
public interface DeploymentResourceDefinitionFactory extends CustomResourceDefinitionFactory<Deployment> {

    @Override
    DeploymentResourceDefinition create();
}
