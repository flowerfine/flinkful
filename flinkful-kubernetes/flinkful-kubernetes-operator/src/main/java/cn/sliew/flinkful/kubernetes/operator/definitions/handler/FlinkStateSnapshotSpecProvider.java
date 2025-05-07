package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkStateSnapshotSpec;

@FunctionalInterface
public interface FlinkStateSnapshotSpecProvider {

    FlinkStateSnapshotSpec getSpec();
}
