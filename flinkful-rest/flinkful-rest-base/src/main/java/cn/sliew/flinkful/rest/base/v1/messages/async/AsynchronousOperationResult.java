package cn.sliew.flinkful.rest.base.v1.messages.async;

import cn.sliew.flinkful.rest.base.v1.messages.queue.QueueStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class AsynchronousOperationResult<V> {

    @JsonProperty("status")
    private QueueStatus queueStatus;

    @Nullable
    @JsonProperty("operation")
    private V value;
}
