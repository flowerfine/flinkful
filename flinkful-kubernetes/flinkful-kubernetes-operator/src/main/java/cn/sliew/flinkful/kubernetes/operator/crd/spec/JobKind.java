package cn.sliew.flinkful.kubernetes.operator.crd.spec;

import cn.sliew.flinkful.kubernetes.operator.crd.CrdConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes the Kubernetes kind of job reference.
 */
public enum JobKind {

    /**
     * FlinkDeployment CR kind.
     */
    @JsonProperty(CrdConstants.KIND_FLINK_DEPLOYMENT)
    FLINK_DEPLOYMENT,

    /**
     * FlinkSessionJob CR kind.
     */
    @JsonProperty(CrdConstants.KIND_SESSION_JOB)
    FLINK_SESSION_JOB
}
