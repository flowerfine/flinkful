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
package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.artifact.Artifact;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.Logging;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class DeploymentParameters extends FlinkResourceParameter {

    private final StorageConfigProperties properties;
    @Singular
    private final List<FileFetcherInitContainerStepDecorator.FileFetcherParam> fileFetcherParams;
    private final Logging logging;
    private Artifact artifact;
    @Builder.Default
    private int parallelism = 1;
}
