package cn.sliew.flinkful.rest.base.v1.messages.async;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.flink.runtime.rest.messages.TriggerId;

@Data
public class TriggerResponse {

    @JsonProperty("request-id")
    private final TriggerId triggerId;
}