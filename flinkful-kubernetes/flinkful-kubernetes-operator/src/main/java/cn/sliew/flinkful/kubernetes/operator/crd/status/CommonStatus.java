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

import cn.sliew.flinkful.kubernetes.operator.crd.lifecycle.ResourceLifecycleState;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.AbstractFlinkSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CommonStatus<SPEC extends AbstractFlinkSpec> {

    /**
     * Last observed status of the Flink job on Application/Session cluster.
     */
    private JobStatus jobStatus = new JobStatus();

    /**
     * Error information about the FlinkDeployment/FlinkSessionJob.
     */
    private String error;

    /**
     * Last observed generation of the FlinkDeployment/FlinkSessionJob.
     */
    private Long observedGeneration;

    /**
     * Lifecycle state of the Flink resource (including being rolled back, failed etc.).
     */
    private ResourceLifecycleState lifecycleState;

    /**
     * Current reconciliation status of this resource.
     *
     * @return Current {@link ReconciliationStatus}.
     */
    public abstract ReconciliationStatus<SPEC> getReconciliationStatus();

    public ResourceLifecycleState getLifecycleState() {
        if (ResourceLifecycleState.DELETING == lifecycleState
                || ResourceLifecycleState.DELETED == lifecycleState) {
            return lifecycleState;
        }

        var reconciliationStatus = getReconciliationStatus();

        if (reconciliationStatus.isBeforeFirstDeployment()) {
            return StringUtils.isEmpty(error)
                    ? ResourceLifecycleState.CREATED
                    : ResourceLifecycleState.FAILED;
        }

        switch (reconciliationStatus.getState()) {
            case UPGRADING:
                return ResourceLifecycleState.UPGRADING;
            case ROLLING_BACK:
                return ResourceLifecycleState.ROLLING_BACK;
        }

        var lastReconciledSpec = reconciliationStatus.deserializeLastReconciledSpec();
        if (lastReconciledSpec.getJob() != null
                && lastReconciledSpec.getJob().getState() == JobState.SUSPENDED) {
            return ResourceLifecycleState.SUSPENDED;
        }

        if (getJobStatus().getState() == org.apache.flink.api.common.JobStatus.FAILED) {
            return ResourceLifecycleState.FAILED;
        }

        if (reconciliationStatus.getState() == ReconciliationState.ROLLED_BACK) {
            return ResourceLifecycleState.ROLLED_BACK;
        } else if (reconciliationStatus.isLastReconciledSpecStable()) {
            return ResourceLifecycleState.STABLE;
        }

        return ResourceLifecycleState.DEPLOYED;
    }
}
