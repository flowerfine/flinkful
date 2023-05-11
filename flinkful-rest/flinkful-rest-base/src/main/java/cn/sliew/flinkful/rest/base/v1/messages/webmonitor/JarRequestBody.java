package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Data
public abstract class JarRequestBody {

    @Nullable
    @JsonProperty("entryClass")
    private String entryClassName;

    @Nullable
    @JsonProperty("programArgs")
    private String programArguments;

    @Nullable
    @JsonProperty("programArgsList")
    private List<String> programArgumentsList;

    @Nullable
    @JsonProperty("parallelism")
    private Integer parallelism;

    @Nullable
    @JsonProperty("jobId")
    private String jobId;

    @Nullable
    @JsonProperty("flinkConfiguration")
    private Map<String, String> flinkConfiguration;
}
