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
package cn.sliew.flinkful.kubernetes.common.dict.cdc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static cn.sliew.flinkful.kubernetes.common.dict.cdc.FlinkCDCPluginName.*;
import static cn.sliew.flinkful.kubernetes.common.dict.cdc.FlinkCDCPluginType.SINK;
import static cn.sliew.flinkful.kubernetes.common.dict.cdc.FlinkCDCPluginType.SOURCE;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkCDCPluginMapping {

    SOURCE_MYSQL(SOURCE, MYSQL),

    SINK_DORIS(SINK, DORIS),
    SINK_STARROCKS(SINK, STARROCKS),

    ROUTE_ROUTE(FlinkCDCPluginType.ROUTE, ROUTE),
    ;

    private final FlinkCDCPluginType pluginType;
    private final FlinkCDCPluginName pluginName;

    public static FlinkCDCPluginMapping of(FlinkCDCPluginName pluginName) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.getPluginName() == pluginName)
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkCDCPluginMapping.class, pluginName.getValue()));
    }
}
