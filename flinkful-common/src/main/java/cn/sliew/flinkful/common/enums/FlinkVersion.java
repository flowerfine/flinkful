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
package cn.sliew.flinkful.common.enums;

import cn.sliew.milky.common.version.SemVersion;

public enum FlinkVersion {

    v1_3_0(1, 3, 0),
    v1_3_1(1, 3, 1),
    v1_3_2(1, 3, 2),
    v1_3_3(1, 3, 3),

    v1_4_0(1, 4, 0),
    v1_4_1(1, 4, 1),
    v1_4_2(1, 4, 2),

    v1_5_0(1, 5, 0),
    v1_5_1(1, 5, 1),
    v1_5_2(1, 5, 2),
    v1_5_3(1, 5, 3),
    v1_5_4(1, 5, 4),
    v1_5_5(1, 5, 5),
    v1_5_6(1, 5, 6),

    v1_6_0(1, 6, 0),
    v1_6_1(1, 6, 1),
    v1_6_2(1, 6, 2),
    v1_6_3(1, 6, 3),
    v1_6_4(1, 6, 4),

    v1_7_0(1, 7, 0),
    v1_7_1(1, 7, 1),
    v1_7_2(1, 7, 2),

    v1_8_0(1, 8, 0),
    v1_8_1(1, 8, 1),
    v1_8_2(1, 8, 2),
    v1_8_3(1, 8, 3),

    v1_9_0(1, 9, 0),
    v1_9_1(1, 9, 1),
    v1_9_2(1, 9, 2),
    v1_9_3(1, 9, 3),

    v1_10_0(1, 10, 0),
    v1_10_1(1, 10, 1),
    v1_10_2(1, 10, 2),
    v1_10_3(1, 10, 3),

    v1_11_0(1, 11, 0),
    v1_11_1(1, 11, 1),
    v1_11_2(1, 11, 2),
    v1_11_3(1, 11, 3),
    v1_11_4(1, 11, 4),
    v1_11_6(1, 11, 6),

    v1_12_0(1, 12, 0),
    v1_12_1(1, 12, 1),
    v1_12_2(1, 12, 2),
    v1_12_3(1, 12, 3),
    v1_12_4(1, 12, 4),
    v1_12_5(1, 12, 5),
    v1_12_7(1, 12, 7),

    v1_13_0(1, 13, 0),
    v1_13_1(1, 13, 1),
    v1_13_2(1, 13, 2),
    v1_13_3(1, 13, 3),
    v1_13_5(1, 13, 5),
    v1_13_6(1, 13, 6),

    v1_14_0(1, 14, 0),
    v1_14_2(1, 14, 2),
    v1_14_3(1, 14, 3),
    v1_14_4(1, 14, 4),
    v1_14_5(1, 14, 5),
    v1_14_6(1, 14, 6),

    v1_15_0(1, 15, 0),
    v1_15_1(1, 15, 1),
    v1_15_2(1, 15, 2),
    v1_15_3(1, 15, 3),
    v1_15_4(1, 15, 4),

    v1_16_0(1, 16, 0),
    v1_16_1(1, 16, 1),
    v1_16_2(1, 16, 2),
    v1_16_3(1, 16, 3),

    v1_17_0(1, 17, 0),
    v1_17_1(1, 17, 1),
    v1_17_2(1, 17, 2),

    v1_18_0(1, 18, 0),
    v1_18_1(1, 18, 1),

    v1_19_0(1, 19, 0),
    v1_19_1(1, 19, 1),
    v1_19_2(1, 19, 2),

    v1_20_0(1, 20, 0),
    v1_20_1(1, 20, 1),

    v2_0_0(2, 0, 0),
    ;

    private int major;
    private int minor;
    private int patch;
    private SemVersion version;

    FlinkVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.version = SemVersion.semVersion(major, minor, patch);
    }

    @Override
    public String toString() {
        return version.toString();
    }
}
