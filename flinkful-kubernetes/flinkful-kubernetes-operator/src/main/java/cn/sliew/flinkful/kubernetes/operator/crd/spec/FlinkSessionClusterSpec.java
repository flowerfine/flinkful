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
import io.fabric8.kubernetes.api.model.Pod;
import lombok.*;

import java.util.Map;

@With
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkSessionClusterSpec {

    /**
     * Flink docker image used to start the Job and TaskManager pods.
     */
    private String image;

    /**
     * Image pull policy of the Flink docker image.
     */
    private String imagePullPolicy;

    /**
     * Kubernetes service used by the Flink deployment.
     */
    private String serviceAccount;

    /**
     * Flink image version.
     */
    private OperatorFlinkVersion flinkVersion;

    /**
     * Ingress specs.
     */
    private IngressSpec ingress;

    /**
     * Base pod template for job and task manager pods. Can be overridden by the jobManager and
     * taskManager pod templates.
     */
    private Pod podTemplate;

    /**
     * JobManager specs.
     */
    private JobManagerSpec jobManager;

    /**
     * TaskManager specs.
     */
    private TaskManagerSpec taskManager;

    /**
     * Flink configuration overrides for the Flink deployment or Flink session job.
     */
    private Map<String, String> flinkConfiguration;

    /**
     * Log configuration overrides for the Flink deployment. Format logConfigFileName ->
     * configContent.
     */
    private Map<String, String> logConfiguration;

    /**
     * Deployment mode of the Flink cluster, native or standalone.
     */
    private KubernetesDeploymentMode mode;

    /**
     * Nonce used to manually trigger restart for the cluster/session job. In order to trigger
     * restart, change the number to anything other than the current value.
     */
    private Long restartNonce;

}
