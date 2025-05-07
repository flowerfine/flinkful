package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.kubernetes.definition.CustomResourceDefinition;
import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;

public interface DeploymentResourceDefinition extends CustomResourceDefinition<Deployment> {

}
