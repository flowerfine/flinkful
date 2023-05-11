package cn.sliew.flinkful.rest.base.v1.messages.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.flink.util.InstantiationUtil;
import org.apache.flink.util.SerializedThrowable;

import java.io.IOException;

public class SerializedThrowableSerializer extends StdSerializer<SerializedThrowable> {

    static final String FIELD_NAME_CLASS = "class";
    static final String FIELD_NAME_STACK_TRACE = "stack-trace";
    public static final String FIELD_NAME_SERIALIZED_THROWABLE = "serialized-throwable";

    public SerializedThrowableSerializer() {
        super(SerializedThrowable.class);
    }

    public void serialize(SerializedThrowable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(FIELD_NAME_CLASS, value.getOriginalErrorClassName());
        gen.writeStringField(FIELD_NAME_STACK_TRACE, value.getFullStringifiedStackTrace());
        gen.writeBinaryField(FIELD_NAME_SERIALIZED_THROWABLE, InstantiationUtil.serializeObject(value));
        gen.writeEndObject();
    }
}
