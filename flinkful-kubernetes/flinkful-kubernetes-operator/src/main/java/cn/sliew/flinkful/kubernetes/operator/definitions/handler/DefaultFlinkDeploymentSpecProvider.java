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
package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.carp.framework.common.dict.k8s.CarpK8sImagePullPolicy;
import cn.sliew.carp.framework.kubernetes.model.ContainerImage;
import cn.sliew.flinkful.kubernetes.common.artifact.Artifact;
import cn.sliew.flinkful.kubernetes.common.dict.operator.FlinkOperatorFlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.*;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.DeploymentServiceStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FileSystemStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FlinkConfigurationStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FlinkStateStorageStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.job.JarJobSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.JobManagerSpecStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FileFetcherMainContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FlinkFileSystemPluginStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FlinkMainContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.PodTemplateStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.Logging;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import cn.sliew.flinkful.kubernetes.operator.util.FlinkConfigurations;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceLabels;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultFlinkDeploymentSpecProvider implements FlinkDeploymentSpecProvider {

    private final DeploymentParameters parameters;

    private final List<FlinkConfigurationStepDecorator> flinkConfigurationStepDecorators;
    private final List<PodTemplateStepDecorator> podTemplateStepDecorators;
    private final List<JobManagerSpecStepDecorator> jobManagerSpecStepDecorators;

    private FlinkDeploymentSpec spec;
    private List<HasMetadata> additionalResources = new ArrayList<>();

    public DefaultFlinkDeploymentSpecProvider(DeploymentParameters parameters) {
        this.parameters = parameters;
        this.flinkConfigurationStepDecorators = Arrays.asList(
                new FileSystemStepDecorator(parameters.getProperties()),
                new FlinkStateStorageStepDecorator(parameters.getProperties(), parameters.getId()),
                new DeploymentServiceStepDecorator()
        );
        this.podTemplateStepDecorators = Arrays.asList(
                new FlinkMainContainerStepDecorator(ResourceLabels.getDeploymentLabels(parameters)),
                new FlinkFileSystemPluginStepDecorator(parameters.getArtifact().getFlinkVersion(), parameters.getProperties()),
                new FileFetcherMainContainerStepDecorator()
        );
        this.jobManagerSpecStepDecorators = Arrays.asList(
                new FileFetcherInitContainerStepDecorator(parameters.getProperties(), parameters.getFileFetcherParams())
        );

        buildSpec();
    }

    private void buildSpec() {
        spec = FlinkDeploymentSpec.builder()
                .imagePullPolicy(getImagePullPolicy().getValue())
                .image(getImage())
                .flinkVersion(getFlinkVersion())
                .serviceAccount(getServiceAccount())
                .jobManager(getJobManagerSpec())
                .taskManager(getTaskManagerSpec())
                .logConfiguration(getLogConfiguration())
                .flinkConfiguration(getFlinkConfiguration())
                .podTemplate(getPodTemplate())
                .mode(KubernetesDeploymentMode.NATIVE)
                .job(getJobSpec())
                .build();
    }

    @Override
    public FlinkDeploymentSpec getSpec() {
        return spec;
    }

    @Override
    public List<HasMetadata> getAdditionalResources() {
        return additionalResources;
    }

    private CarpK8sImagePullPolicy getImagePullPolicy() {
        return parameters.getArtifact().getContainerImage().getImagePullPolicy();
    }

    private String getImage() {
        ContainerImage containerImage = parameters.getArtifact().getContainerImage();
        return containerImage.getImage();
    }

    private OperatorFlinkVersion getFlinkVersion() {
        Artifact artifact = parameters.getArtifact();
        FlinkOperatorFlinkVersion flinkOperatorFlinkVersion = FlinkOperatorFlinkVersion.of(artifact.getFlinkVersion());
        return EnumUtils.getEnum(OperatorFlinkVersion.class, flinkOperatorFlinkVersion.getValue());
    }

    private String getServiceAccount() {
        return "flink";
    }

    private JobManagerSpec getJobManagerSpec() {
        JobManagerSpec spec = JobManagerSpec.builder()
                .resource(new Resource(1.0, "1G", null))
                .replicas(1)
                .build();
        for (JobManagerSpecStepDecorator decorator : jobManagerSpecStepDecorators) {
            spec = decorator.decorate(spec);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return spec;
    }

    private TaskManagerSpec getTaskManagerSpec() {
        return TaskManagerSpec.builder()
                .resource(new Resource(1.0, "1G", null))
                .replicas(1)
                .build();
    }

    private Map<String, String> getLogConfiguration() {
        Logging logging = parameters.getLogging();
        if (logging != null) {
            return Map.of(logging.getFileName(), logging.getFileContent());
        }
        return null;
    }

    private Map<String, String> getFlinkConfiguration() {
        Map<String, String> flinkConfiguration = FlinkConfigurations.createFlinkConfiguration();
        for (FlinkConfigurationStepDecorator decorator : flinkConfigurationStepDecorators) {
            flinkConfiguration = decorator.decorate(flinkConfiguration);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return flinkConfiguration;
    }

    private Pod getPodTemplate() {
        Pod podTemplate = new PodBuilder().build();
        for (PodTemplateStepDecorator decorator : podTemplateStepDecorators) {
            podTemplate = decorator.decorate(podTemplate);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return podTemplate;
    }

    private JobSpec getJobSpec() {
        return new JarJobSpecProvider(parameters).getJobSpec();
    }
}
