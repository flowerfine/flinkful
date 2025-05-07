package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.kubernetes.definition.CustomResourceDefinitionFactory;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;

@FunctionalInterface
public interface SessionClusterResourceDefinitionFactory extends CustomResourceDefinitionFactory<SessionCluster> {

    @Override
    SessionClusterResourceDefinition create();
}
