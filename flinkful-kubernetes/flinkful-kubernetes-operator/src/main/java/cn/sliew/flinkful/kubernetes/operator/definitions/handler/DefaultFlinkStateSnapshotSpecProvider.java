package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.FlinkStateSnapshotSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobReference;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.SavepointSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.status.SavepointFormatType;
import cn.sliew.flinkful.kubernetes.operator.parameters.StateSnapshotParameters;
import org.apache.commons.lang3.EnumUtils;

public class DefaultFlinkStateSnapshotSpecProvider implements FlinkStateSnapshotSpecProvider {

    private final StateSnapshotParameters parameters;

    private FlinkStateSnapshotSpec spec;

    public DefaultFlinkStateSnapshotSpecProvider(StateSnapshotParameters parameters) {
        this.parameters = parameters;
        buildSpec();
    }

    private void buildSpec() {
        spec = FlinkStateSnapshotSpec.builder()
                .backoffLimit(1)
                .jobReference(JobReference.builder()
                        .kind(parameters.getJobReferKind())
                        .name(parameters.getJobReferName())
                        .build())
                .savepoint(SavepointSpec.builder()
                        .alreadyExists(false)
                        .disposeOnDelete(parameters.isDisposeOnDelete())
                        .formatType(EnumUtils.getEnum(SavepointFormatType.class, parameters.getFormatType().getValue()))
                        .build())
                .build();
    }

    @Override
    public FlinkStateSnapshotSpec getSpec() {
        return spec;
    }
}
