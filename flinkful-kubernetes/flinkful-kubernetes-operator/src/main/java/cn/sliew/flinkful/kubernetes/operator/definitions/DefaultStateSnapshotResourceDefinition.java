package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot.FlinkStateSnapshot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DefaultStateSnapshotResourceDefinition implements StateSnapshotResourceDefinition {

    private final FlinkStateSnapshot resource;
}
