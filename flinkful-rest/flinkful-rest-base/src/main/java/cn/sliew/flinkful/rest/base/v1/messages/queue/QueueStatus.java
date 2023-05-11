package cn.sliew.flinkful.rest.base.v1.messages.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueueStatus {

    @JsonProperty(value = "id", required = true)
    private Id id;

    public enum Id {
        IN_PROGRESS, COMPLETED;
    }
}
