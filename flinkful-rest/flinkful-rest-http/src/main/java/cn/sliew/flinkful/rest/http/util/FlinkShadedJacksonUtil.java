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
package cn.sliew.flinkful.rest.http.util;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.rest.util.RestMapperUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JavaType;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.type.CollectionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public enum FlinkShadedJacksonUtil {
    ;

    public static final ObjectMapper OBJECT_MAPPER = RestMapperUtils.getStrictObjectMapper();
    static {
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            log.error("json 序列化失败 object: {}", object, var2);
            Rethrower.throwAs(var2);
            return null;
        }
    }

    public static <T> T parseJsonString(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException var3) {
            log.error("json 反序列化失败 clazz: {}, json: {}", new Object[]{clazz.getName(), json, var3});
            Rethrower.throwAs(var3);
            return null;
        }
    }

    public static <T> T parseJsonString(String json, Class<T> outerType, Class parameterClasses) {
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(outerType, parameterClasses);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("json 反序列化失败 clazz: {}, json: {}", outerType.getTypeName(), json, e);
            Rethrower.throwAs(e);
            return null;
        }
    }

    public static <T> List<T> parseJsonArray(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        } else {
            try {
                CollectionType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
                return OBJECT_MAPPER.readValue(json, listType);
            } catch (Exception var3) {
                log.error("json 反序列化为 list 失败 clazz: {}, json: {}", new Object[]{clazz.getName(), json, var3});
                return Collections.emptyList();
            }
        }
    }
}
