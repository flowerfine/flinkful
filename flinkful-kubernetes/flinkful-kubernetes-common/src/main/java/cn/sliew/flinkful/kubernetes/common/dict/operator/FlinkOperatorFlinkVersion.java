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
package cn.sliew.flinkful.kubernetes.common.dict.operator;

import cn.sliew.carp.framework.common.dict.DictInstance;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;

import static cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion.*;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkOperatorFlinkVersion implements DictInstance {

    v1_15("v1_15", "v1_15", V_1_15_4, V_1_15_4, V_1_15_3, V_1_15_2, V_1_15_1, V_1_15_0),
    v1_16("v1_16", "v1_16", V_1_16_3, V_1_16_3, V_1_16_2, V_1_16_1, V_1_16_0),
    v1_17("v1_17", "v1_17", V_1_17_2, V_1_17_2, V_1_17_1, V_1_17_0),
    v1_18("v1_18", "v1_18", V_1_18_1, V_1_18_1, V_1_18_0),
    v1_19("v1_19", "v1_19", V_1_19_2, V_1_19_2, V_1_19_1, V_1_19_0),
    v1_20("v1_20", "v1_20", V_1_20_1, V_1_20_1, V_1_20_0),
    v2_0("v2_0", "v2_0", V_2_0_0, V_2_0_0),
    ;

    @JsonCreator
    public static FlinkOperatorFlinkVersion of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkOperatorFlinkVersion.class, value));
    }

    @EnumValue
    private final String value;
    private final String label;
    private final FlinkVersion defaultVersion;
    private final FlinkVersion[] supportVersions;

    FlinkOperatorFlinkVersion(String value, String label, FlinkVersion defaultVersion, FlinkVersion... supportVersions) {
        this.value = value;
        this.label = label;
        this.defaultVersion = defaultVersion;
        this.supportVersions = supportVersions;
    }

    public static FlinkOperatorFlinkVersion of(FlinkVersion flinkVersion) {
        for (FlinkOperatorFlinkVersion operatorFlinkVersion : values()) {
            for (FlinkVersion version : operatorFlinkVersion.getSupportVersions()) {
                if (version == flinkVersion) {
                    return operatorFlinkVersion;
                }
            }
        }
        throw new IllegalStateException("unknown flink version for: " + flinkVersion.getValue());
    }
}
