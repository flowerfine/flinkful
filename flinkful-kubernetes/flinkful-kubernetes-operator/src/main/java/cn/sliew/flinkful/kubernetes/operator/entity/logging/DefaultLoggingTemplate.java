package cn.sliew.flinkful.kubernetes.operator.entity.logging;

import cn.sliew.carp.framework.spring.util.ResourceUtil;

import java.io.IOException;

public enum DefaultLoggingTemplate {
    ;

    private static final String DEFAULT_TEMPLATE_NAME = "default";
    private static final String DEFAULT_TEMPLATE_PATH = "/default-flink-log4j.tpl";

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
}
