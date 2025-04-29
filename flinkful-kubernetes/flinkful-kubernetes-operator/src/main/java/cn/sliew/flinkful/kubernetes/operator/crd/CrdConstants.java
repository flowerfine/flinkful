package cn.sliew.flinkful.kubernetes.operator.crd;

/**
 * Constants used by the CRD.
 */
public class CrdConstants {

    public static final String API_GROUP = "flink.apache.org";
    public static final String API_VERSION = "v1beta1";
    public static final String KIND_SESSION_JOB = "FlinkSessionJob";
    public static final String KIND_FLINK_DEPLOYMENT = "FlinkDeployment";
    public static final String KIND_FLINK_STATE_SNAPSHOT = "FlinkStateSnapshot";

    public static final String LABEL_TARGET_SESSION = "target.session";

    public static final String EPHEMERAL_STORAGE = "ephemeral-storage";

    public static final String LABEL_SNAPSHOT_TYPE = "snapshot.type";
    public static final String LABEL_SNAPSHOT_TRIGGER_TYPE = "snapshot.trigger-type";
    public static final String LABEL_SNAPSHOT_STATE = "snapshot.state";
    public static final String LABEL_SNAPSHOT_JOB_REFERENCE_KIND = "job-reference.kind";
    public static final String LABEL_SNAPSHOT_JOB_REFERENCE_NAME = "job-reference.name";
}
