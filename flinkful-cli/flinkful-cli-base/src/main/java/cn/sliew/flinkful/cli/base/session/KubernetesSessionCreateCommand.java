/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;

import java.nio.file.Path;

/**
 * Native Kubernetes 部署需要利用 ${user.home}/.kube/config 信息获取 Kubernetes 信息
 */
@Slf4j
public class KubernetesSessionCreateCommand implements SessionCommand {

    @Override
    public ClusterClient create(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration) throws Exception {
        KubernetesClusterDescriptor clusterDescriptor = (KubernetesClusterDescriptor) FlinkUtil.createClusterDescriptor(configuration);
        ClusterSpecification clusterSpecification = FlinkUtil.createClusterSpecification(configuration);
        return createClusterClient(clusterDescriptor, clusterSpecification);
    }

    private ClusterClient<String> createClusterClient(KubernetesClusterDescriptor clusterDescriptor,
                                                      ClusterSpecification clusterSpecification) throws ClusterDeploymentException {

        ClusterClientProvider<String> provider = clusterDescriptor.deploySessionCluster(clusterSpecification);
        ClusterClient<String> clusterClient = provider.getClusterClient();

        log.info("deploy session with clusterId: {}", clusterClient.getClusterId());
        return clusterClient;
    }
}
