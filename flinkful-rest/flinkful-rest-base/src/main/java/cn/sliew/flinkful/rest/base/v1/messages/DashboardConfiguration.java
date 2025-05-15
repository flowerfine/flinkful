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
package cn.sliew.flinkful.rest.base.v1.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DashboardConfiguration {

    @JsonProperty("refresh-interval")
    private long refreshInterval;

    @JsonProperty("timezone-name")
    private String timeZoneName;

    @JsonProperty("timezone-offset")
    private int timeZoneOffset;

    @JsonProperty("flink-version")
    private String flinkVersion;

    @JsonProperty("flink-revision")
    private String flinkRevision;

    @JsonProperty("features")
    private Features features;

    @Data
    public static final class Features {

        @JsonProperty("web-submit")
        private boolean webSubmitEnabled;

        @JsonProperty("web-cancel")
        private boolean webCancelEnabled;

        @JsonProperty("web-history")
        private boolean isHistoryServer;
    }
}