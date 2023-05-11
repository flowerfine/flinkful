package cn.sliew.flinkful.rest.base.v1.messages.async;

import cn.sliew.flinkful.rest.base.v1.messages.json.SerializedThrowableSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.flink.util.SerializedThrowable;

import javax.annotation.Nullable;

@Data
public class AsynchronousOperationInfo {

    @Nullable
    @JsonProperty("failure-cause")
    @JsonSerialize(using = SerializedThrowableSerializer.class)
    private SerializedThrowable failureCause;
}
