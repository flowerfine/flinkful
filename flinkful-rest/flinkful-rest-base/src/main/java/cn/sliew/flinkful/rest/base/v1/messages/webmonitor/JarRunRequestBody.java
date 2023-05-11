package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class JarRunRequestBody extends JarRequestBody {

    @Nullable
    @JsonProperty("allowNonRestoredState")
    private Boolean allowNonRestoredState;

    @Nullable
    @JsonProperty("savepointPath")
    private String savepointPath;

    @Nullable
    @JsonProperty("restoreMode")
    private RestoreMode restoreMode;
}