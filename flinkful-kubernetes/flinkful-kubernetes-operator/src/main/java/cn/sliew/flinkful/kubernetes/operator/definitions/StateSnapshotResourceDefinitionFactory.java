package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.kubernetes.definition.CustomResourceDefinitionFactory;
import cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot.FlinkStateSnapshot;

@FunctionalInterface
public interface StateSnapshotResourceDefinitionFactory extends CustomResourceDefinitionFactory<FlinkStateSnapshot> {

    @Override
    StateSnapshotResourceDefinition create();
}
