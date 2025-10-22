package cn.sliew.flinkful.sql.gateway.embedded.service.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsoleUpdateParam extends ConsoleAddParam {

    @NotNull
    private Long id;
}
