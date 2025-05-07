package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SessionClusterParameters extends FlinkResourceParameter {

    private final FlinkVersion flinkVersion;
}
