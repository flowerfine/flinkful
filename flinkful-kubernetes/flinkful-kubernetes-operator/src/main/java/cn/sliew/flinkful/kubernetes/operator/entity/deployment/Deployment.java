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
package cn.sliew.flinkful.kubernetes.operator.entity.deployment;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkDeploymentSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.FlinkDeploymentStatus;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment {

    private final String kind = ResourceKinds.DEPLOYMENT;
    private final String apiVersion = ResourceVersions.FLINK_VERSION;
    private final DeploymentMetadata metadata;
    private final FlinkDeploymentSpec spec;
    private final FlinkDeploymentStatus status;

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class DeploymentMetadata {

        private final String id;
        private final String name;
        private final String namespace;
        private final Map<String, String> labels;
        private final Map<String, String> annotations;
        private final Integer resourceVersion;
    }

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class DeploymentMetadataLabels {

        private final String system;
        private final String internalNamespace;
        private final String app;
        private final String instance;
        private final String component;

        private final String deploymentId;
        private final String deploymentName;
        private final String jobId;
    }
}
