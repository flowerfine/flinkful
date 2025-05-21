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
package cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster;

import cn.sliew.carp.framework.kubernetes.resources.DeploymentResource;
import cn.sliew.carp.framework.kubernetes.resources.GenericKubernetesResourceKubernetesResource;
import cn.sliew.carp.framework.kubernetes.resources.KubernetesResourceWithChild;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;

public class SessionClusterResource
        extends GenericKubernetesResourceKubernetesResource
        implements KubernetesResourceWithChild<GenericKubernetesResource, DeploymentResource> {

    private SessionCluster sessionCluster;

    public SessionClusterResource(KubernetesClient client, GenericKubernetesResource origin, SessionCluster sessionCluster) {
        super(client, origin);
        this.sessionCluster = sessionCluster;
    }

    @Override
    public DeploymentResource getChild() {
        // flink kubernetes operator 自己的 FlinkDeployment
        // 和最终 flink native kubernetes 部署的 Deployment 的 name 一致
        // 因此直接用 name 关联，如果将来发生变动，需重新调整
        Deployment origin = new DeploymentBuilder()
                .withNewMetadata()
                .withName(sessionCluster.getMetadata().getName())
                .withNamespace(sessionCluster.getMetadata().getNamespace())
                .endMetadata()
                .build();
        Map<String, String> podLabels = sessionCluster.getSpec().getPodTemplate().getMetadata().getLabels();
        return new DeploymentResource(client, origin, podLabels);
    }
}
