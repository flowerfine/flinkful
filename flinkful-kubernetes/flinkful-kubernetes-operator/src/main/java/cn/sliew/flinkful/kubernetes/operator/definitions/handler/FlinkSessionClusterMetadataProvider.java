package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;

@FunctionalInterface
public interface FlinkSessionClusterMetadataProvider {

    SessionCluster.SessionClusterMetadata getMetadata();
}
