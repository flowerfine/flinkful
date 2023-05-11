package cn.sliew.flinkful.rest.base.v1.messages.async;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TriggerResponse {

    @JsonProperty("request-id")
    private final String triggerId;
}