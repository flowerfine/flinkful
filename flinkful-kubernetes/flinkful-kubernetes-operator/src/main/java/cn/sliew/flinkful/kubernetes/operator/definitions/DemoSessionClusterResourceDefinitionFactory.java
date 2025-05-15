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
package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.DefaultLoggingTemplate;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.Log4jTemplate;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.LoggerPair;
import cn.sliew.flinkful.kubernetes.operator.entity.logging.Logging;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import cn.sliew.flinkful.kubernetes.operator.parameters.SessionClusterParameters;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceLabels;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoSessionClusterResourceDefinitionFactory implements SessionClusterResourceDefinitionFactory {

    public static final UUID DEFAULT_SESSION_CLUSTER_ID = UUID.fromString("dac4ca57-6dd2-3168-3d27-7e23a91c85d7");
    public static final String DEFAULT_SESSION_CLUSTER_NAME = "test-session-cluster" + DEFAULT_SESSION_CLUSTER_ID;

    @Override
    public SessionClusterResourceDefinition create() {
        StorageConfigProperties properties = new StorageConfigProperties();
        properties.setType("s3");
        S3ConfigProperties s3 = new S3ConfigProperties();
        s3.setBucket("carp");
        s3.setEndpoint("http://127.0.0.1:9000");
        s3.setAccessKey("admin");
        s3.setSecretKey("password");
        properties.setS3(s3);


        LoggerPair loggerPair = new LoggerPair();
        loggerPair.setLogger("cn.sliew");
        loggerPair.setLevel(Level.INFO.name());
        Log4jTemplate log4jTemplate = DefaultLoggingTemplate.DEFAULT_PROFILE.toBuilder().log4jLogger(loggerPair).build();
        Logging logging = DefaultLoggingTemplate.buildLogger(log4jTemplate);


        SessionClusterParameters parameters = SessionClusterParameters.builder()
                .id(DEFAULT_SESSION_CLUSTER_ID)
                .name(StringUtils.truncate(StringUtils.replace(DEFAULT_SESSION_CLUSTER_NAME, "-", ""), 45))
                .namespace("default")
                .internalNamespace("default")
                .flinkVersion(FlinkVersion.V_1_18_1)
                .properties(properties)
                .logging(logging)
                .build();

        FlinkSessionClusterMetadataProvider flinkSessionClusterMetadataProvider = getFlinkSessionClusterMetadataProvider(parameters);
        FlinkSessionClusterSpecProvider flinkSessionClusterSpecProvider = getFlinkSessionClusterSpecProvider(parameters);
        SessionCluster sessionCluster = SessionCluster.builder()
                .metadata(flinkSessionClusterMetadataProvider.getMetadata())
                .spec(flinkSessionClusterSpecProvider.getSpec())
                .build();
        List<HasMetadata> additionalResources = flinkSessionClusterSpecProvider.getAdditionalResources();
        return new DefaultSessionClusterResourceDefinition(sessionCluster, additionalResources);
    }

    private FlinkSessionClusterMetadataProvider getFlinkSessionClusterMetadataProvider(SessionClusterParameters parameters) {
        return () -> {
            return SessionCluster.SessionClusterMetadata.builder()
                    .name(parameters.getName())
                    .namespace(parameters.getNamespace())
                    .labels(ResourceLabels.getSessionClusterLabels(parameters))
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkSessionClusterSpecProvider getFlinkSessionClusterSpecProvider(SessionClusterParameters parameters) {
        return new DefaultFlinkSessionClusterSpecProvider(parameters);
    }
}
