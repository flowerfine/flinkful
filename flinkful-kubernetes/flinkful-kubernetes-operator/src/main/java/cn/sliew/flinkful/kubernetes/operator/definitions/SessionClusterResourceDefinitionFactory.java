package cn.sliew.flinkful.kubernetes.operator.definitions;

@FunctionalInterface
public interface SessionClusterResourceDefinitionFactory {

    SessionClusterResourceDefinition create();
}
