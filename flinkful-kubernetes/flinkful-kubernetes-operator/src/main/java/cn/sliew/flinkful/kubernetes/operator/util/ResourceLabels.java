package cn.sliew.flinkful.kubernetes.operator.util;

import cn.sliew.carp.framework.kubernetes.label.LabelNames;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import cn.sliew.flinkful.kubernetes.operator.parameters.FlinkResourceParameter;
import cn.sliew.flinkful.kubernetes.operator.parameters.StateSnapshotParameters;
import cn.sliew.flinkful.kubernetes.operator.parameters.SessionClusterParameters;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum ResourceLabels {
    ;

    public static final String DEPLOYMENT_ID = "deploymentId";
    public static final String DEPLOYMENT_NAME = "deploymentName";
    public static final String SESSION_CLUSTER_ID = "sessionClusterId";
    public static final String SESSION_CLUSTER_NAME = "sessionClusterName";
    public static final String JOB_ID = "jobId";
    public static final String STATE_SNAPSHOT_ID = "stateSnapshotId";

    public static Map<String, String> getCommonsLabels(FlinkResourceParameter parameter) {
        return Map.of(LabelNames.SYSTEM_LABEL, "flinkful",
                LabelNames.APP_LABEL, "flink",
                LabelNames.INSTANCE_LABEL, parameter.getId().toString(),
                LabelNames.INTERNAL_NAMESPACE_LABEL, parameter.getInternalNamespace(),
                LabelNames.UUID_LABEL, parameter.getId().toString(),
                LabelNames.NAME_LABEL, parameter.getName()
        );
    }

    public static Map<String, String> getSessionClusterLabels(SessionClusterParameters parameters) {
        return ImmutableMap.<String, String>builder()
                .putAll(getCommonsLabels(parameters))
                .put(LabelNames.COMPONENT_LABEL, "session-cluster")
                .put(SESSION_CLUSTER_ID, parameters.getId().toString())
                .put(SESSION_CLUSTER_NAME, parameters.getName())
                .build();
    }

    public static Map<String, String> getDeploymentLabels(DeploymentParameters parameters) {
        return ImmutableMap.<String, String>builder()
                .putAll(getCommonsLabels(parameters))
                .put(LabelNames.COMPONENT_LABEL, "deployment")
                .put(DEPLOYMENT_ID, parameters.getId().toString())
                .put(DEPLOYMENT_NAME, parameters.getName())
                .build();
    }

    public static Map<String, String> getStateSnapshotLabels(StateSnapshotParameters parameters) {
        return ImmutableMap.<String, String>builder()
                .putAll(getCommonsLabels(parameters))
                .put(LabelNames.COMPONENT_LABEL, "state-snapshot")
                .put(STATE_SNAPSHOT_ID, parameters.getId().toString())
                .put(JOB_ID, parameters.getJobReferId().toString())
                .build();
    }
}
