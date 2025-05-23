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
package cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec;

import cn.sliew.carp.framework.common.dict.k8s.CarpK8sImagePullPolicy;
import cn.sliew.carp.framework.common.util.NetUtil;
import cn.sliew.carp.framework.storage.config.HdfsConfigProperties;
import cn.sliew.carp.framework.storage.config.OSSConfigProperties;
import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobManagerSpec;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceNames;
import cn.sliew.milky.common.util.JacksonUtil;
import io.fabric8.kubernetes.api.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class FileFetcherInitContainerStepDecorator extends AbstractJobManagerSpecStepDecorator {

    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_REQUEST = Map.of(
            "cpu", Quantity.parse("0.25"),
            "memory", Quantity.parse("512Mi"));
    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_LIMIT = Map.of(
            "cpu", Quantity.parse("1.0"),
            "memory", Quantity.parse("2Gi")
    );

    public static final String ENV_S3_ENDPOINT = "S3_ENDPOINT";
    public static final String ENV_S3_ACCESS = "AWS_ACCESS_KEY_ID";
    public static final String ENV_S3_SECRET = "AWS_SECRET_ACCESS_KEY";

    public static final String ENV_OSS_ENDPOINT = "OSS_ENDPOINT";
    public static final String ENV_OSS_ACCESS = "OSS_ACCESS_KEY_ID";
    public static final String ENV_OSS_SECRET = "OSS_ACCESS_KEY_SECRET";

    private final StorageConfigProperties properties;
    private final List<FileFetcherParam> files;

    @Override
    public JobManagerSpec decorate(JobManagerSpec spec) {
        return spec.toBuilder()
                .resource(spec.getResource())
                .replicas(spec.getReplicas())
                .podTemplate(buildPod(spec.getPodTemplate()))
                .build();
    }

    private Pod buildPod(Pod podTemplate) {
        PodBuilder podBuilder = Optional.ofNullable(podTemplate)
                .map(pod -> new PodBuilder(pod))
                .orElse(new PodBuilder());

        podBuilder.editOrNewMetadata()
                .withName(ResourceNames.JOB_MANAGER_POD_TEMPLATE_NAME)
                .endMetadata();
        addFileFetcherInitContainers(podBuilder);

        return podBuilder.build();
    }

    private void addFileFetcherInitContainers(PodBuilder builder) {
        builder.editOrNewSpec()
                .addToInitContainers(buildInitContainer())
                .endSpec();
    }

    private Container buildInitContainer() {
        ContainerBuilder builder = new ContainerBuilder();
        builder.withName(ResourceNames.FILE_FETCHER_CONTAINER_NAME);
        builder.withImage(ResourceNames.FILE_FETCHER_CONTAINER_IMAGE);
        builder.withImagePullPolicy(CarpK8sImagePullPolicy.IF_NOT_PRESENT.getValue());
        builder.withArgs(buildFileFetcherArgs());
        builder.withEnv(buildEnvs());
        builder.withResources(buildResource());
        builder.withVolumeMounts(buildVolumeMount());
        builder.withTerminationMessagePath("/dev/termination-log");
        builder.withTerminationMessagePolicy("File");
        return builder.build();
    }

    private List<String> buildFileFetcherArgs() {
        return Arrays.asList("-file-fetcher-json", JacksonUtil.toJsonString(files));
    }

    private List<EnvVar> buildEnvs() {
        List<EnvVar> envs = new ArrayList<>();
        if (properties.getS3() != null) {
            S3ConfigProperties s3 = properties.getS3();
            envs.add(new EnvVarBuilder().withName(ENV_S3_ENDPOINT).withValue(NetUtil.replaceLocalhost(s3.getEndpoint())).build());
            envs.add(new EnvVarBuilder().withName(ENV_S3_ACCESS).withValue(s3.getAccessKey()).build());
            envs.add(new EnvVarBuilder().withName(ENV_S3_SECRET).withValue(s3.getSecretKey()).build());
        }
        if (properties.getOss() != null) {
            OSSConfigProperties oss = properties.getOss();
            envs.add(new EnvVarBuilder().withName(ENV_OSS_ENDPOINT).withValue(oss.getEndpoint()).build());
            envs.add(new EnvVarBuilder().withName(ENV_OSS_ACCESS).withValue(oss.getAccessKey()).build());
            envs.add(new EnvVarBuilder().withName(ENV_OSS_SECRET).withValue(oss.getSecretKey()).build());
        }
        if (properties.getHdfs() != null) {
            HdfsConfigProperties hdfs = properties.getHdfs();
            // todo
        }
        return envs;
    }

    private ResourceRequirements buildResource() {
        ResourceRequirementsBuilder resourceRequirementsBuilder = new ResourceRequirementsBuilder();
        resourceRequirementsBuilder.addToRequests(FILE_FETCHER_CONTAINER_REQUEST);
        resourceRequirementsBuilder.addToLimits(FILE_FETCHER_CONTAINER_LIMIT);
        return resourceRequirementsBuilder.build();
    }

    private List<VolumeMount> buildVolumeMount() {
        VolumeMountBuilder jarDir = new VolumeMountBuilder();
        jarDir.withName(ResourceNames.FILE_FETCHER_FLINKFUL_JAR_VOLUME_NAME);
        jarDir.withMountPath(ResourceNames.FLINKFUL_JAR_DIRECTORY);

        VolumeMountBuilder usrlibDir = new VolumeMountBuilder();
        usrlibDir.withName(ResourceNames.FILE_FETCHER_FLINKFUL_USRLIB_VOLUME_NAME);
        usrlibDir.withMountPath(ResourceNames.FLINKFUL_USRLIB_DIRECTORY);
        return Arrays.asList(jarDir.build(), usrlibDir.build());
    }

    @Data
    @AllArgsConstructor
    public static class FileFetcherParam {
        private String uri;
        private String path;
    }
}
