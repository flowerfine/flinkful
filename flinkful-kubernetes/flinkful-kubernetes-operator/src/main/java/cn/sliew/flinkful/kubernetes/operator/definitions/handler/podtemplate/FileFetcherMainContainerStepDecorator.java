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
package cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate;

import cn.sliew.carp.framework.kubernetes.util.ContainerUtil;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceNames;
import io.fabric8.kubernetes.api.model.*;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class FileFetcherMainContainerStepDecorator extends AbstractPodTemplateStepDecorator {

    private static final String ENV_JAVA_OPTS_ALL_NAME = "CLASSPATH";

    @Override
    public Pod decorate(Pod podTemplate) {
        PodBuilder podBuilder = new PodBuilder(podTemplate);

        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = podBuilder.editOrNewSpec();
        spec.addAllToVolumes(buildVolume()); // add volumes
        ContainerUtil.getOrCreateContainer(spec, ResourceNames.FLINK_MAIN_CONTAINER_NAME)
                .addAllToEnv(buildJavaOptsEnv())
                .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                .endContainer();
        spec.endSpec();

        return podBuilder.build();
    }

    private List<EnvVar> buildJavaOptsEnv() {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName(ENV_JAVA_OPTS_ALL_NAME);
        builder.withValue(ResourceNames.FLINKFUL_USRLIB_DIRECTORY);
        return Collections.singletonList(builder.build());
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

    private List<Volume> buildVolume() {
        VolumeBuilder jarDir = new VolumeBuilder();
        jarDir.withName(ResourceNames.FILE_FETCHER_FLINKFUL_JAR_VOLUME_NAME);
        jarDir.withEmptyDir(new EmptyDirVolumeSource());

        VolumeBuilder usrlibJar = new VolumeBuilder();
        usrlibJar.withName(ResourceNames.FILE_FETCHER_FLINKFUL_USRLIB_VOLUME_NAME);
        usrlibJar.withEmptyDir(new EmptyDirVolumeSource());
        return Arrays.asList(jarDir.build(), usrlibJar.build());
    }
}
