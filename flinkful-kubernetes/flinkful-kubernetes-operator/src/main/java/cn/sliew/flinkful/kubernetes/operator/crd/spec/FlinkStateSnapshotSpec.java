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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Spec that describes a FlinkStateSnapshot.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkStateSnapshotSpec implements Diffable<FlinkStateSnapshotSpec> {

    /**
     * Source to take a snapshot of. Not required if it's a savepoint and alreadyExists is true.
     */
    private JobReference jobReference;

    /**
     * Spec in case of savepoint.
     */
    private SavepointSpec savepoint = null;

    /**
     * Spec in case of checkpoint.
     */
    private CheckpointSpec checkpoint = null;

    /**
     * Maximum number of retries before the snapshot is considered as failed. Set to -1 for
     * unlimited or 0 for no retries.
     */
    private int backoffLimit = -1;

    public boolean isSavepoint() {
        return savepoint != null;
    }

    public boolean isCheckpoint() {
        return checkpoint != null;
    }

    @Override
    public DiffResult diff(FlinkStateSnapshotSpec right) {
        ReflectionDiffBuilder builder = new ReflectionDiffBuilder(this, right, ToStringStyle.DEFAULT_STYLE);
        return builder.build();
    }
}
