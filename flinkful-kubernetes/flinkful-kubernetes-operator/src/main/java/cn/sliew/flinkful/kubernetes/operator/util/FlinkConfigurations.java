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
package cn.sliew.flinkful.kubernetes.operator.util;

import cn.sliew.flinkful.kubernetes.common.dict.*;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.IngressSpec;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions;

import java.util.HashMap;
import java.util.Map;

public enum FlinkConfigurations {
    ;

    public static Map<String, String> createFlinkConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put(WebOptions.CANCEL_ENABLE.key(), "true");
        // deprecated
        flinkConfiguration.put(AkkaOptions.ASK_TIMEOUT_DURATION.key(), "100s");
        flinkConfiguration.put(RpcOptions.ASK_TIMEOUT_DURATION.key(), "100s");
        flinkConfiguration.put(TaskManagerOptions.SLOT_TIMEOUT.key(), "100s");
        flinkConfiguration.putAll(createFailureTolerateConfiguration());
        flinkConfiguration.putAll(createCheckpointConfiguration());
        flinkConfiguration.putAll(createPeriodicSavepointConfiguration());
        flinkConfiguration.putAll(createRestartConfiguration());
        flinkConfiguration.putAll(createMetricsReporterConfiguration());
        return flinkConfiguration;
    }

    private static Map<String, String> createFailureTolerateConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put(RestartStrategyOptions.RESTART_STRATEGY.key(), FlinkRestartStrategy.FAILURE_RATE.getValue());
        flinkConfiguration.put(RestartStrategyOptions.RESTART_STRATEGY_FAILURE_RATE_FAILURE_RATE_INTERVAL.key(), "10min");
        flinkConfiguration.put(RestartStrategyOptions.RESTART_STRATEGY_FAILURE_RATE_MAX_FAILURES_PER_INTERVAL.key(), "30");
        flinkConfiguration.put(RestartStrategyOptions.RESTART_STRATEGY_FAILURE_RATE_DELAY.key(), "10s");
        return flinkConfiguration;
    }

    private static Map<String, String> createCheckpointConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put(ExecutionCheckpointingOptions.CHECKPOINTING_MODE.key(), FlinkSemantic.EXACTLY_ONCE.getValue());
        flinkConfiguration.put(ExecutionCheckpointingOptions.CHECKPOINTING_INTERVAL.key(), "3min");
        flinkConfiguration.put(ExecutionCheckpointingOptions.MAX_CONCURRENT_CHECKPOINTS.key(), "1");
        flinkConfiguration.put(ExecutionCheckpointingOptions.MIN_PAUSE_BETWEEN_CHECKPOINTS.key(), "3min");
        flinkConfiguration.put(ExecutionCheckpointingOptions.CHECKPOINTING_TIMEOUT.key(), "18min");
        flinkConfiguration.put(ExecutionCheckpointingOptions.EXTERNALIZED_CHECKPOINT.key(), FlinkCheckpointRetain.RETAIN_ON_CANCELLATION.getValue());
        flinkConfiguration.put(CheckpointingOptions.MAX_RETAINED_CHECKPOINTS.key(), "10");
        return flinkConfiguration;
    }

    private static Map<String, String> createPeriodicSavepointConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.operator.savepoint.format.type", FlinkSavepointType.NATIVE.getValue());
        flinkConfiguration.put("kubernetes.operator.periodic.savepoint.interval", "1h");
        flinkConfiguration.put("kubernetes.operator.savepoint.history.max.count", "24");
        flinkConfiguration.put("kubernetes.operator.savepoint.history.max.age", "72h");
        flinkConfiguration.put("kubernetes.operator.savepoint.trigger.grace-period", "20min");
        return flinkConfiguration;
    }

    private static Map<String, String> createRestartConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.enabled", "true");
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.restarts.window", "3d");
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.restarts.threshold", "12");
        return flinkConfiguration;
    }

    public static Map<String, String> createDeploymentServiceConfiguration() {
        Map<String, String> serviceConfiguration = new HashMap<>();
        serviceConfiguration.put(KubernetesConfigOptions.REST_SERVICE_EXPOSED_TYPE.key(), ServiceExposedType.NODE_PORT.getValue());
        serviceConfiguration.put(KubernetesConfigOptions.REST_SERVICE_EXPOSED_NODE_PORT_ADDRESS_TYPE.key(), NodePortAddressType.EXTERNAL_IP.getValue());
        return serviceConfiguration;
    }

    public static Map<String, String> createSessionClusterConfiguration() {
        Map<String, String> serviceConfiguration = new HashMap<>();
        serviceConfiguration.put(KubernetesConfigOptions.REST_SERVICE_EXPOSED_TYPE.key(), ServiceExposedType.LOAD_BALANCER.getValue());
        return serviceConfiguration;
    }

    private static Map<String, String> createLogConfiguration() {
        Map<String, String> logConfiguration = new HashMap<>();
        return logConfiguration;
    }

    private static Map<String, String> createMetricsReporterConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put(MetricOptions.REPORTERS_LIST.key(), "jmx, prom");
        flinkConfiguration.put("metrics.reporter.jmx.factory.class", "org.apache.flink.metrics.jmx.JMXReporterFactory");
        flinkConfiguration.put("metrics.reporter.jmx.port", "8789");
        flinkConfiguration.put("metrics.reporter.prom.factory.class", "org.apache.flink.metrics.prometheus.PrometheusReporterFactory");
        flinkConfiguration.put("metrics.reporter.prom.port", "9249");

        // 多个值配置方式：a1:v1,a2:v2
        flinkConfiguration.put(KubernetesConfigOptions.JOB_MANAGER_ANNOTATIONS.key(), "prometheus.io/port:9249,prometheus.io/scrape:true");
        flinkConfiguration.put(KubernetesConfigOptions.TASK_MANAGER_ANNOTATIONS.key(), "prometheus.io/port:9249,prometheus.io/scrape:true");
        return flinkConfiguration;
    }

    public static IngressSpec createIngressSpec() {
        IngressSpec spec = new IngressSpec();
        spec.setTemplate("/{{namespace}}/{{name}}(/|$)(.*)");
        spec.setClassName("nginx");
        Map<String, String> annotations = new HashMap<>();
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");
        spec.setAnnotations(annotations);
        return spec;
    }

}
