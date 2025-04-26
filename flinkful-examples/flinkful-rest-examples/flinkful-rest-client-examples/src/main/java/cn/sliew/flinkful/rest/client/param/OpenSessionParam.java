package cn.sliew.flinkful.rest.client.param;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OpenSessionParam {

    @Nullable
    private String sessionName;

    @Nullable
    private Map<String, String> properties;
}
