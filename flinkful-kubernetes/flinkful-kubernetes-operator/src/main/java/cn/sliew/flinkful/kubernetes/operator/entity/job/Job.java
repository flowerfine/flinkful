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
package cn.sliew.flinkful.kubernetes.operator.entity.job;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.JobStatus;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.Objects;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
public final class Job {

    private final String kind = ResourceKinds.JOB;
    private final String apiVersion = ResourceVersions.FLINK_VERSION;
    private final JobMetadata metadata;
    private final JobSpec spec;
    private final JobStatus status;

    @JsonIgnore
    public Boolean isSessionMode() {
        return Objects.nonNull(metadata) && Objects.nonNull(metadata.getSessionClusterId());
    }

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class JobMetadata {

        private final String id;
        private final String name;
        private final String namespace;

        private final String deploymentId;
        private final String deploymentName;
        private final String sessionClusterId;
        private final String sessionClusterName;

        private final Map<String, String> labels;
        private final Map<String, String> annotations;
        private final Integer resourceVersion;
    }
}
