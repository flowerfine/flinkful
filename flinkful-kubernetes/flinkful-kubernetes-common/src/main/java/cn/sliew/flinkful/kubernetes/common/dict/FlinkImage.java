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
package cn.sliew.flinkful.kubernetes.common.dict;

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType.*;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkImage implements DictInstance {

    JAR_1_15(JAR, FlinkVersion.V_1_15_4, "flink:1.15.4-scala_2.12-java8", "flink:1.15.4-scala_2.12-java8"),
    JAR_1_16(JAR, FlinkVersion.V_1_16_3, "flink:1.16.3-scala_2.12-java8", "flink:1.16.3-scala_2.12-java8"),
    JAR_1_17(JAR, FlinkVersion.V_1_17_2, "flink:1.17.2-scala_2.12-java8", "flink:1.17.2-scala_2.12-java8"),
    JAR_1_18(JAR, FlinkVersion.V_1_18_1, "flink:1.18.1-scala_2.12-java8", "flink:1.18.1-scala_2.12-java8"),
    JAR_1_19(JAR, FlinkVersion.V_1_19_2, "flink:1.19.2-scala_2.12-java8", "flink:1.19.2-scala_2.12-java8"),
    JAR_1_20(JAR, FlinkVersion.V_1_20_1, "flink:1.20.1-scala_2.12-java8", "flink:1.20.1-scala_2.12-java8"),
    JAR_2_0(JAR, FlinkVersion.V_2_0_0, "flink:2.0.0-scala_2.12-java17", "flink:2.0.0-scala_2.12-java17"),

    SQL_1_17(SQL, FlinkVersion.V_1_17_2, "ghcr.io/flowerfine/scaleph-sql-template:1.17", "ghcr.io/flowerfine/scaleph-sql-template:1.17"),
    SQL_1_18(SQL, FlinkVersion.V_1_18_1, "ghcr.io/flowerfine/scaleph-sql-template:1.18", "ghcr.io/flowerfine/scaleph-sql-template:1.19"),
//    SQL_1_19(SQL, FlinkVersion.V_1_19_2, "2.0.0-scala_2.12-java17", "2.0.0-scala_2.12-java17"),
//    SQL_1_20(SQL, FlinkVersion.V_1_20_1, "2.0.0-scala_2.12-java17", "2.0.0-scala_2.12-java17"),
//    SQL_2_0(SQL, FlinkVersion.V_2_0_0, "2.0.0-scala_2.12-java17", "2.0.0-scala_2.12-java17"),

    FLINK_CDC_1_18(FLINK_CDC, FlinkVersion.V_1_18_1, "ghcr.io/flowerfine/scaleph-flink-cdc:3.0.0-flink-1.18", "ghcr.io/flowerfine/scaleph-flink-cdc:3.0.0-flink-1.18"),

    SEATUNNEL_1_16(SEATUNNEL, FlinkVersion.V_1_16_3, "ghcr.io/flowerfine/scaleph-seatunnel:2.3.8-flink-1.16", "ghcr.io/flowerfine/scaleph-seatunnel:2.3.8-flink-1.16"),
    ;

    @JsonCreator
    public static FlinkImage of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkImage.class, value));
    }

    private final FlinkJobType jobType;
    private final FlinkVersion version;
    @EnumValue
    private final String value;
    private final String label;

    public static FlinkImage ofFlinkVersion(FlinkJobType jobType, FlinkVersion version) {
        for (FlinkImage image : values()) {
            if (image.getJobType().equals(jobType) && image.getVersion().equals(version)) {
                return image;
            }
        }
        throw new EnumConstantNotPresentException(FlinkImage.class, String.format("FlinkJobType: %s, Version: %s", jobType.getValue(), version.getValue()));
    }
}
