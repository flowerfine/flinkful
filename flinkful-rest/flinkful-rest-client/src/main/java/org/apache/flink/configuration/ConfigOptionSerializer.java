/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flink.configuration;

import cn.sliew.milky.common.exception.Rethrower;
import org.apache.flink.configuration.description.Formatter;
import org.apache.flink.configuration.description.HtmlFormatter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonGenerator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ConfigOptionSerializer extends StdSerializer<ConfigOption> {

    private final Formatter formatter = new HtmlFormatter();

    public ConfigOptionSerializer() {
        super(ConfigOption.class);
    }

    @Override
    public void serialize(ConfigOption option, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("key", option.key());
        jsonGenerator.writeStringField("clazz", option.getClazz().getSimpleName());
        jsonGenerator.writeStringField("description", formatter.format(option.description()));
        jsonGenerator.writeBooleanField("list", option.isList());
        if (option.hasDefaultValue()) {
            jsonGenerator.writeObjectField("defaultValue", option.defaultValue());
        }
        if (option.hasFallbackKeys()) {
            jsonGenerator.writeArrayFieldStart("fallbackKeys");
            Iterable<FallbackKey> iterable = option.fallbackKeys();
            iterable.forEach(fallbackKey -> {
                try {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("key", fallbackKey.getKey());
                    jsonGenerator.writeBooleanField("deprecated", fallbackKey.isDeprecated());
                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    Rethrower.throwAs(e);
                }
            });
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
