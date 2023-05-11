package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JarRunResponseBody {

    @JsonProperty("jobid")
    private String jobId;
}