package cn.sliew.flinkful.sql.gateway.controller.param;

import lombok.Getter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Map;

@Getter
public class ExecuteStatementParam {

    @JsonProperty("statement")
    private final String statement;
    @JsonProperty("executionTimeout")
    @Nullable
    private final Long timeout;
    @JsonProperty("executionConfig")
    @Nullable
    private final Map<String, String> executionConfig;

    public ExecuteStatementParam(@JsonProperty("statement") String statement,
                                 @Nullable @JsonProperty("executionTimeout") Long timeout,
                                 @Nullable @JsonProperty("executionConfig") Map<String, String> executionConfig) {
        this.statement = statement;
        this.timeout = timeout;
        this.executionConfig = executionConfig;
    }
}
