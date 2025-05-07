package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.List;

public interface DeploymentResourceDefinition {

    Deployment getDeployment();

    List<HasMetadata> getAdditionalResources();
}
