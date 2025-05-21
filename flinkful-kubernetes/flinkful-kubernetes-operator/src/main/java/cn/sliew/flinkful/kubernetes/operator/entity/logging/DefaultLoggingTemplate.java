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
package cn.sliew.flinkful.kubernetes.operator.entity.logging;

import cn.sliew.carp.framework.spring.util.ResourceUtil;
import cn.sliew.carp.framework.template.jinja.JinjaFactory;
import cn.sliew.milky.common.util.JacksonUtil;
import com.hubspot.jinjava.Jinjava;

import java.io.IOException;
import java.util.Map;

public enum DefaultLoggingTemplate {
    ;

    private static final String DEFAULT_TEMPLATE_NAME = "default";
    private static final String DEFAULT_LOGBACK_TEMPLATE_PATH = "/default-logback-console.tpl";
    private static final String DEFAULT_LOG4J_TEMPLATE_PATH = "/default-log4j-console.tpl";
    private static final String DEFAULT_TEMPLATE_PATH = DEFAULT_LOGBACK_TEMPLATE_PATH;

    public static final LogTemplate DEFAULT_PROFILE = LogTemplate.builder()
            .name(DEFAULT_TEMPLATE_NAME)
            .template(loadDefaultTemplate())
            .build();

    private static String loadDefaultTemplate() {
        try {
            return ResourceUtil.loadClassPathResource(DEFAULT_TEMPLATE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Load default default template error", e);
        }
    }

    public static Logging buildLogger(String fileName, LogTemplate template) {
        Jinjava jinjava = JinjaFactory.getInstance();
        Map<String, Object> params = JacksonUtil.toMap(JacksonUtil.toJsonNode(template));
        String renderedFileContent = jinjava.render(template.getTemplate(), params);
        return Logging.builder()
                .fileName(fileName)
                .fileContent(renderedFileContent)
                .build();
    }
}
