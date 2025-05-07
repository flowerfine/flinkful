package cn.sliew.flinkful.kubernetes.operator.definitions;

@FunctionalInterface
public interface DeploymentResourceDefinitionFactory {

    DeploymentResourceDefinition create();
}
