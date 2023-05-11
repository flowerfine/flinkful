package cn.sliew.flinkful.rest.base.v1.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

@Data
public class JobPlanInfo {

    @JsonProperty("plan")
    private final RawJson jsonPlan;

    @Data
    @JsonSerialize(using = RawJson.Serializer.class)
    @JsonDeserialize(using = RawJson.Deserializer.class)
    public static final class RawJson {

        private String json;

        private RawJson(String json) {
            this.json = json;
        }

        public static final class Deserializer extends StdDeserializer<RawJson> {

            public Deserializer() {
                super(RawJson.class);
            }

            public RawJson deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                JsonNode rootNode = jsonParser.readValueAsTree();
                return new RawJson(rootNode.toString());
            }
        }

        public static final class Serializer extends StdSerializer<RawJson> {

            public Serializer() {
                super(RawJson.class);
            }

            public void serialize(RawJson jobPlanInfo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeRawValue(jobPlanInfo.json);
            }
        }
    }
}