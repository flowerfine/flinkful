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

import cn.sliew.carp.framework.kubernetes.annotation.AnnotationNames;
import cn.sliew.carp.framework.kubernetes.util.ContainerUtil;
import cn.sliew.flinkful.kubernetes.operator.util.Ports;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceNames;
import io.fabric8.kubernetes.api.model.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class FlinkMainContainerStepDecorator extends AbstractPodTemplateStepDecorator {

    private final Map<String, String> labels;

    @Override
    public Pod decorate(Pod podTemplate) {
        PodBuilder podBuilder = new PodBuilder(podTemplate);

        podBuilder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .addToAnnotations(buildAnnotations())
                .addToLabels(labels)
                .endMetadata();
        handlePodTemplate(podBuilder);

        return podBuilder.build();
    }

    private void handlePodTemplate(PodBuilder builder) {
        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        ContainerUtil.getOrCreateContainer(spec, ResourceNames.FLINK_MAIN_CONTAINER_NAME)
                .addAllToPorts(buildMetricsPorts())
                .addAllToEnv(buildEnv())
                .endContainer();

        spec.endSpec();
    }

    private Map<String, String> buildAnnotations() {
        Map<String, String> annotations = new HashMap<>();
        annotations.put(AnnotationNames.PROMETHEUS_ANNOTATION_PORT, AnnotationNames.PROMETHEUS_ANNOTATION_PORT_VALUE);
        annotations.put(AnnotationNames.PROMETHEUS_ANNOTATION_SCRAPE, AnnotationNames.PROMETHEUS_ANNOTATION_SCRAPE_VALUE);
        return Collections.emptyMap();
    }

    private List<ContainerPort> buildMetricsPorts() {
        return Ports.FLINK_METRICS_PORTS;
    }

    private List<EnvVar> buildEnv() {
        List<EnvVar> envs = new ArrayList<>();
        envs.add(new EnvVarBuilder().withName("TZ").withValue("Asia/Shanghai").build());
        return envs;
    }
}
