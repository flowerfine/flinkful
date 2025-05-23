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
package cn.sliew.flinkful.kubernetes.operator.crd.spec;

/**
 * Enumeration for supported Flink versions.
 */
public enum OperatorFlinkVersion {

    /**
     * Deprecated since 1.10 operator release.
     */
    @Deprecated
    v1_15(1, 15),
    /**
     * Deprecated since 1.11 operator release.
     */
    @Deprecated
    v1_16(1, 16),

    v1_17(1, 17),
    v1_18(1, 18),
    v1_19(1, 19),
    v1_20(1, 20),
    v2_0(2, 0);

    /**
     * The major integer from the Flink semver. For example for Flink 1.18.1 this would be 1.
     */
    private final int majorVersion;

    /**
     * The minor integer from the Flink semver. For example for Flink 1.18.1 this would be 18.
     */
    private final int minorVersion;

    OperatorFlinkVersion(int major, int minor) {
        this.majorVersion = major;
        this.minorVersion = minor;
    }

    public boolean isEqualOrNewer(OperatorFlinkVersion otherVersion) {
        if (this.majorVersion > otherVersion.majorVersion) {
            return true;
        }
        if (this.majorVersion == otherVersion.majorVersion) {
            return this.minorVersion >= otherVersion.minorVersion;
        }
        return false;
    }

    public static boolean isSupported(OperatorFlinkVersion version) {
        return version != null && version.isEqualOrNewer(OperatorFlinkVersion.v1_15);
    }

    /**
     * Returns the FlinkVersion associated with the supplied major and minor version integers.
     *
     * @param major The major part of the Flink version (e.g. 1 for 1.18.1).
     * @param minor The minor part of the Flink version (e.g. 18 for 1.18.1).
     * @return The FlinkVersion associated with the supplied major and minor version integers.
     * @throws IllegalArgumentException If the supplied major and minor version do not correspond to
     *                                  a supported FlinkVersion.
     */
    public static OperatorFlinkVersion fromMajorMinor(int major, int minor) {
        for (OperatorFlinkVersion version : values()) {
            if (version.majorVersion == major && version.minorVersion == minor) {
                return version;
            }
        }
        throw new IllegalArgumentException("Unknown Flink version: " + major + "." + minor);
    }
}
