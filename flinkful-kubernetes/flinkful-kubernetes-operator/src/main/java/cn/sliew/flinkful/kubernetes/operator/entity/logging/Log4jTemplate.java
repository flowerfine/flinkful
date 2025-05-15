package cn.sliew.flinkful.kubernetes.operator.entity.logging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.apache.logging.log4j.Level;

import java.util.List;

@Data
@With
@Jacksonized
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Log4jTemplate {

    public static final String ROOT_LOGGER_LEVEL = Level.INFO.name();

    private final String name;
    private final String log4jTemplate;
    @Builder.Default
    private final String rootLoggerLevel = ROOT_LOGGER_LEVEL;
    @Singular
    private final List<LoggerPair> log4jLoggers;
}
