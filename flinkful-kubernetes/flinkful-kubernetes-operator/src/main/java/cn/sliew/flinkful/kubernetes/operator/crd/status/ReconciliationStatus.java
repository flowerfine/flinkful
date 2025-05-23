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

import cn.sliew.flinkful.kubernetes.operator.crd.AbstractFlinkResource;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.AbstractFlinkSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.util.SpecUtils;
import cn.sliew.flinkful.kubernetes.operator.crd.util.SpecWithMeta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Status of the last reconcile step for the FlinkDeployment/FlinkSessionJob.
 */
@Data
@NoArgsConstructor
public abstract class ReconciliationStatus<SPEC extends AbstractFlinkSpec> {

    /**
     * Epoch timestamp of the last successful reconcile operation.
     */
    private long reconciliationTimestamp;

    /**
     * Last reconciled deployment spec. Used to decide whether further reconciliation steps are
     * necessary.
     */
    private String lastReconciledSpec;

    /**
     * Last stable deployment spec according to the specified stability condition. If a rollback
     * strategy is defined this will be the target to roll back to.
     */
    private String lastStableSpec;

    /**
     * Deployment state of the last reconciled spec.
     */
    private ReconciliationState state = ReconciliationState.UPGRADING;

    @JsonIgnore
    public abstract Class<SPEC> getSpecClass();

    @JsonIgnore
    public SPEC deserializeLastReconciledSpec() {
        var specWithMeta = deserializeLastReconciledSpecWithMeta();
        return specWithMeta != null ? specWithMeta.getSpec() : null;
    }

    @JsonIgnore
    public SPEC deserializeLastStableSpec() {
        var specWithMeta = deserializeLastStableSpecWithMeta();
        return specWithMeta != null ? specWithMeta.getSpec() : null;
    }

    @JsonIgnore
    public SpecWithMeta<SPEC> deserializeLastReconciledSpecWithMeta() {
        return SpecUtils.deserializeSpecWithMeta(lastReconciledSpec, getSpecClass());
    }

    @JsonIgnore
    public SpecWithMeta<SPEC> deserializeLastStableSpecWithMeta() {
        return SpecUtils.deserializeSpecWithMeta(lastStableSpec, getSpecClass());
    }

    @JsonIgnore
    public void serializeAndSetLastReconciledSpec(
            SPEC spec, AbstractFlinkResource<SPEC, ?> resource) {
        setLastReconciledSpec(SpecUtils.writeSpecWithMeta(spec, resource));
        resource.getStatus().setObservedGeneration(resource.getMetadata().getGeneration());
    }

    public void markReconciledSpecAsStable() {
        lastStableSpec = lastReconciledSpec;
    }

    @JsonIgnore
    public boolean isLastReconciledSpecStable() {
        if (lastReconciledSpec == null || lastStableSpec == null) {
            return false;
        }
        return lastReconciledSpec.equals(lastStableSpec);
    }

    @JsonIgnore
    public boolean isBeforeFirstDeployment() {
        return lastReconciledSpec == null;
    }
}
