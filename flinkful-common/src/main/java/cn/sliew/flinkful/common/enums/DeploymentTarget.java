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
package cn.sliew.flinkful.common.enums;

import lombok.Getter;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.core.execution.PipelineExecutorFactory;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;

@Getter
public enum DeploymentTarget {

    STANDALONE_APPLICATION(0, ResourceProvider.STANDALONE, DeploymentMode.APPLICATION, RemoteExecutor.NAME),
    STANDALONE_SESSION(1, ResourceProvider.STANDALONE, DeploymentMode.SESSION, RemoteExecutor.NAME),

    /**
     * 以 kubernetes 作为 resource provider 时需要在提交机器上提前设置好 kubeconfig 文件，供 flink 连接 kubernetes 集群。
     */
    NATIVE_KUBERNETES_APPLICATION(2, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.APPLICATION, KubernetesDeploymentTarget.APPLICATION.getName()),
    NATIVE_KUBERNETES_SESSION(3, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.SESSION, KubernetesDeploymentTarget.SESSION.getName()),
    ;

    private int code;
    private ResourceProvider resourceProvider;
    private DeploymentMode deploymentMode;

    /**
     * @see PipelineExecutorFactory#getName()
     */
    private String name;

    DeploymentTarget(int code, ResourceProvider resourceProvider, DeploymentMode deploymentMode, String name) {
        this.code = code;
        this.resourceProvider = resourceProvider;
        this.deploymentMode = deploymentMode;
        this.name = name;
    }

    public void apply(Configuration configuration) {
        configuration.set(DeploymentOptions.TARGET, getName());
    }
}
