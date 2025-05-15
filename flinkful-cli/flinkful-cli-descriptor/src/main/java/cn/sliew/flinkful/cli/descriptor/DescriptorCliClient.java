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
package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.submit.CommandFactory;
import cn.sliew.flinkful.cli.descriptor.submit.SubmitCommand;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public class DescriptorCliClient implements CliClient {

    @Override
    public ClusterClient submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case STANDALONE_SESSION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(flinkHome, configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public ClusterClient submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_APPLICATION:
            case STANDALONE_APPLICATION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(flinkHome, configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
