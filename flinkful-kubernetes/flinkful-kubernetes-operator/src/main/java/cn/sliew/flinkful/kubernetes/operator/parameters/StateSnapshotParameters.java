package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.flinkful.kubernetes.common.dict.operator.SavepointFormatType;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobKind;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public class StateSnapshotParameters extends FlinkResourceParameter {

    private UUID jobReferId;
    private JobKind jobReferKind;
    private String jobReferName;

    private boolean disposeOnDelete;
    private SavepointFormatType formatType;
}
