package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot.FlinkStateSnapshot;

@FunctionalInterface
public interface FlinkStateSnapshotMetadataProvider {

    FlinkStateSnapshot.FlinkStateSnapshotMetadata getMetadata();
}
