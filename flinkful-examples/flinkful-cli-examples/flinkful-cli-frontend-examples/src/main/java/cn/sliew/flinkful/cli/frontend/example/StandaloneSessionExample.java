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
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.configuration.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class StandaloneSessionExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        client.submit(DeploymentTarget.STANDALONE_SESSION, null, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 通过 {@link PipelineOptions#JARS} 将任务 jar 包和对应的依赖都可以一起传到 JobManager。
     * 在提交任务时，除了任务本身 jar 包外，还可以添加额外的依赖 jar 包。
     * 如 seatunnel 的 flink-jdbc-connector 因为开源协议兼容的问题，是不包含 jdbc Driver 的，所以提交
     * seatunnel 任务时需要一起提交 seatunnel-core-flink.jar 和 mysql-connector-java.jar。
     * 通过 {@link PipelineOptions#JARS} 就可以实现这个功能。
     *
     * @see PackagedProgram#getJobJarAndDependencies()
     */
    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(JobManagerOptions.ADDRESS, "localhost");
        configuration.setInteger(JobManagerOptions.PORT, 6123);
        configuration.setInteger(RestOptions.PORT, 8081);
        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }
}
