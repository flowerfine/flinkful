package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;

@FunctionalInterface
public interface FlinkDeploymentMetadataProvider {

    Deployment.DeploymentMetadata getMetadata();
}
