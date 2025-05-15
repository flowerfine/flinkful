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
package cn.sliew.flinkful.kubernetes.operator;

import cn.sliew.flinkful.kubernetes.operator.definitions.*;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.kubeclient.FlinkKubeClientFactory;
import org.apache.flink.kubernetes.shaded.io.fabric8.kubernetes.client.NamespacedKubernetesClient;


public class TestMain {

    public static void main(String[] args) {
        String yaml = testSessionCluster();
//        String yaml = testDeployment();
//        String yaml = testStateSnapshot();
        System.out.println(yaml);

//        NamespacedKubernetesClient fabric8ioKubernetesClient = getKubernetesClient();
//        fabric8ioKubernetesClient
//                .load(new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)))
//                .createOrReplace();
    }

    private static String testSessionCluster() {
        DemoSessionClusterResourceDefinitionFactory sessionClusterResourceDefinitionFactory = new DemoSessionClusterResourceDefinitionFactory();
        SessionClusterResourceDefinition sessionClusterResourceDefinition = sessionClusterResourceDefinitionFactory.create();
        return Serialization.asJson(sessionClusterResourceDefinition.getResource());
    }

    private static String testDeployment() {
        DemoDeploymentResourceDefinitionFactory demoDeploymentResourceDefinitionFactory = new DemoDeploymentResourceDefinitionFactory();
        DeploymentResourceDefinition deploymentResourceDefinition = demoDeploymentResourceDefinitionFactory.create();
        return Serialization.asYaml(deploymentResourceDefinition.getResource());
    }

    private static String testStateSnapshot() {
        DemoStateSnapshotResourceDefinitionFactory demoStateSnapshotResourceDefinitionFactory = new DemoStateSnapshotResourceDefinitionFactory();
        StateSnapshotResourceDefinition stateSnapshotResourceDefinition = demoStateSnapshotResourceDefinitionFactory.create();
        return Serialization.asYaml(stateSnapshotResourceDefinition.getResource());
    }

    private static NamespacedKubernetesClient getKubernetesClient() {
        FlinkKubeClientFactory flinkKubeClientFactory = FlinkKubeClientFactory.getInstance();
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        return flinkKubeClientFactory.createFabric8ioKubernetesClient(configuration);
    }
}
