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
package cn.sliew.flinkful.kubernetes.operator.entity.filefetcher;

import cn.sliew.flinkful.kubernetes.operator.util.ResourceKinds;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceVersions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.fabric8.kubernetes.api.model.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Jacksonized
@Builder(toBuilder = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFetcher {

    private final String kind = ResourceKinds.FILE_FETCHER;
    private final String apiVersion = ResourceVersions.FLINKFUL_VERSION;
    private final FileFetcherMetadata metadata;
    private final FileFetcherSpec spec;

    @Data
    @Jacksonized
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FileFetcherMetadata {

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
    public static final class FileFetcherSpec {

        private final ContainerImage image;
        @Builder.Default
        private final List<Volume> volumes = Collections.emptyList();
        @Builder.Default
        private final List<VolumeMount> volumeMounts = Collections.emptyList();
        @Builder.Default
        private final List<EnvVar> envVars = Collections.emptyList();
        @Builder.Default
        private final List<String> args = Collections.emptyList();
        @Builder.Default
        private ResourceRequirements resources = new ResourceRequirements();
    }
}
