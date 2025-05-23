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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JobStatus {

    /**
     * Name of the job.
     */
    private String jobName;

    /**
     * Flink JobId of the Job.
     */
    private String jobId;

    /**
     * Last observed state of the job.
     */
    private org.apache.flink.api.common.JobStatus state;

    /**
     * Start time of the job.
     */
    private String startTime;

    /**
     * Update time of the job.
     */
    private String updateTime;

    private String upgradeSavepointPath;

    /**
     * Information about pending and last savepoint for the job.
     */
    @Deprecated
    private SavepointInfo savepointInfo = new SavepointInfo();

    /**
     * Information about pending and last checkpoint for the job.
     */
    @Deprecated
    private CheckpointInfo checkpointInfo = new CheckpointInfo();
}