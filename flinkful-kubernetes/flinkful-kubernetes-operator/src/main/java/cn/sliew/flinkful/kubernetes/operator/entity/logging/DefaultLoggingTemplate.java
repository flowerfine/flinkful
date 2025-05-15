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
    private static final String DEFAULT_TEMPLATE_PATH = "/default-log4j-console.tpl";

    public static final Log4jTemplate DEFAULT_PROFILE = Log4jTemplate.builder()
            .name(DEFAULT_TEMPLATE_NAME)
            .log4jTemplate(loadDefaultTemplate())
            .build();

    private static String loadDefaultTemplate() {
        try {
            return ResourceUtil.loadClassPathResource(DEFAULT_TEMPLATE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Load default log4j template error", e);
        }
    }

    public static Logging buildLogger(Log4jTemplate log4jTemplate) {
        Jinjava jinjava = JinjaFactory.getInstance();
        Map<String, Object> params = JacksonUtil.toMap(JacksonUtil.toJsonNode(log4jTemplate));
        String renderedFileContent = jinjava.render(log4jTemplate.getLog4jTemplate(), params);
        return Logging.builder()
                .fileName(Logging.LOG4j_CONSOLE_PROPERTIES)
                .fileContent(renderedFileContent)
                .build();
    }
}
