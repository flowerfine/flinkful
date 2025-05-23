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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum to control Flink job upgrade behavior.
 */
public enum UpgradeMode {

    /**
     * Job is upgraded by first taking a savepoint of the running job, shutting it down and
     * restoring from the savepoint.
     */
    @JsonProperty("savepoint")
    SAVEPOINT,

    /**
     * Job is upgraded using any latest checkpoint or savepoint available.
     */
    @JsonProperty("last-state")
    LAST_STATE,

    /**
     * Job is upgraded with empty state.
     */
    @JsonProperty("stateless")
    STATELESS
}
