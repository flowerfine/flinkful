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
package cn.sliew.flinkful.cli.frontend;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.ClientUtils;
import org.apache.flink.client.cli.ApplicationDeployer;
import org.apache.flink.client.deployment.ClusterClientServiceLoader;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.deployment.application.cli.ApplicationClusterDeployer;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;

import java.nio.file.Path;

public class FrontendCliClient implements CliClient {

    private final ClusterClientServiceLoader clusterClientServiceLoader = new DefaultClusterClientServiceLoader();
    private final ApplicationDeployer deployer = new ApplicationClusterDeployer(clusterClientServiceLoader);

    private final PipelineExecutorServiceLoader pipelineExecutorServiceLoader = new DefaultExecutorServiceLoader();

    /**
     * refer CliFrontend#run(String[])
     */
    @Override
    public ClusterClient submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        try (PackagedProgram program = FlinkUtil.buildProgram(configuration, job)) {
            ClientUtils.executeProgram(pipelineExecutorServiceLoader, configuration, program, false, false);
        }
        return null;
    }

    /**
     * refer CliFrontend#runApplication(String[])
     */
    @Override
    public ClusterClient submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        deployer.run(configuration, applicationConfiguration);
        return null;
    }
}
