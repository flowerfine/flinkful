package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.kubernetes.definition.CustomResourceDefinition;
import cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot.FlinkStateSnapshot;

public interface StateSnapshotResourceDefinition extends CustomResourceDefinition<FlinkStateSnapshot> {

}
