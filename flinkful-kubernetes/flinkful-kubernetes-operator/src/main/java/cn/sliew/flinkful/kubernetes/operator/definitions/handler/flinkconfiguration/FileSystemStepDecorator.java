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
package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.carp.framework.common.util.NetUtil;
import cn.sliew.carp.framework.storage.config.OSSConfigProperties;
import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FlinkFileSystemPluginStepDecorator;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理文件系统
 */
@RequiredArgsConstructor
public class FileSystemStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    private final StorageConfigProperties properties;

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

        addFileSystemConfigOption(flinkConfiguration);

        return flinkConfiguration;
    }

    void addFileSystemConfigOption(Map<String, String> flinkConfiguration) {
        if (properties.getS3() != null) {
            S3ConfigProperties s3 = properties.getS3();
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.S3_ENDPOINT, NetUtil.replaceLocalhost(s3.getEndpoint()));
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.S3_ACCESS_KEY, s3.getAccessKey());
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.S3_SECRET_KEY, s3.getSecretKey());
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.S3_PATH_STYLE_ACCESS, "true"); // container
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.FS_ALLOWED_FALLBACK_FILESYSTEM, "s3"); // container
        }

        if (properties.getOss() != null) {
            OSSConfigProperties oss = properties.getOss();
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.OSS_ENDPOINT, oss.getEndpoint());
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.OSS_ACCESS_KEY, oss.getAccessKey());
            flinkConfiguration.put(FlinkFileSystemPluginStepDecorator.OSS_SECRET_KEY, oss.getSecretKey());
        }
    }
}
