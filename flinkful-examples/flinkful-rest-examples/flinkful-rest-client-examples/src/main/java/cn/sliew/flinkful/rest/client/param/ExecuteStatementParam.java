package cn.sliew.flinkful.rest.client.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
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
