package cn.sliew.flinkful.rest.client.param;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
public class ExecuteStatementParam {

    @NotBlank
    private String statement;

    @Nullable
    private Long timeout;

    @Nullable
    private Map<String, String> executionConfig;
}
