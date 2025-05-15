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
package cn.sliew.flinkful.cli.frontend.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class NativeKubernetesSessionExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, null, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 在已经创建好的 flink-kubernetes-session 集群中提交任务，只需要设置 clusterId 即可。
     * 获取 clusterId 的方式有很多：
     *     1. 创建 flink-kubernetes-session 集群时可以获取 clusterId。
     *     2. clusterId 即为 flink-kubernetes-session 集群所在 pod 的 deployment id。可以从 kubernetes 中获取。
     */
    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(KubernetesConfigOptions.CLUSTER_ID, "flink-cluster-e40a0927cfe98fb3bf5c428a9583e60");
        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
