package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkSessionClusterSpec;

@FunctionalInterface
public interface FlinkSessionClusterSpecProvider {

    FlinkSessionClusterSpec getSpec();
}
