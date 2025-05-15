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
package cn.sliew.flinkful.cli.descriptor.example.session;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.SessionClient;
import cn.sliew.flinkful.cli.descriptor.example.Util;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

public class NativeKubernetesSessionCreateExample {

    public static void main(String[] args) throws Exception {
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.15.1");
        ClusterClient<String> clusterClient = SessionClient.create(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHome, buildSessionConfiguration());
        CliClient client = Util.buildCliClient();
        client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHome, buildConfiguration(clusterClient.getClusterId()), Util.buildJarJob());
    }

    private static Configuration buildSessionConfiguration() {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.set(KubernetesConfigOptions.REST_SERVICE_EXPOSED_TYPE, KubernetesConfigOptions.ServiceExposedType.LoadBalancer);
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.set(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        return configuration;
    }

    /**
     * 同样需要设置 JobManager 和 TaskManager 的总资源
     */
    private static Configuration buildConfiguration(String clusterId) throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(KubernetesConfigOptions.CLUSTER_ID, clusterId);
        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
