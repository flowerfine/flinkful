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
package cn.sliew.flinkful.kubernetes.operator.crd.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.flink.annotation.Experimental;

/**
 * Last observed status of the Flink state snapshot.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkStateSnapshotStatus implements Diffable<FlinkStateSnapshotStatus> {

    /**
     * Current state of the snapshot.
     */
    private State state = State.TRIGGER_PENDING;

    /**
     * Trigger ID of the snapshot.
     */
    private String triggerId;

    /**
     * Trigger timestamp of a pending snapshot operation.
     */
    private String triggerTimestamp;

    /**
     * Timestamp when the snapshot was last created/failed.
     */
    private String resultTimestamp;

    /**
     * Final path of the snapshot.
     */
    private String path;

    /**
     * Optional error information about the FlinkStateSnapshot.
     */
    private String error;

    /**
     * Number of failures, used for tracking max retries.
     */
    private int failures = 0;

    /**
     * Describes state of a snapshot.
     */
    @Experimental
    public enum State {
        /**
         * Snapshot was successful and available.
         */
        COMPLETED,

        /**
         * Error during snapshot.
         */
        FAILED,

        /**
         * Snapshot in progress.
         */
        IN_PROGRESS,

        /**
         * Not yet processed by the operator.
         */
        TRIGGER_PENDING,

        /**
         * Snapshot abandoned due to job failure/upgrade.
         */
        ABANDONED
    }

    @Override
    public DiffResult diff(FlinkStateSnapshotStatus right) {
        ReflectionDiffBuilder builder = new ReflectionDiffBuilder(this, right, ToStringStyle.DEFAULT_STYLE);
        return builder.build();
    }
}
