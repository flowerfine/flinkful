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
