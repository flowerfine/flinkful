package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.flinkful.kubernetes.common.artifact.Artifact;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DeploymentParameters extends FlinkResourceParameter {

    private Artifact artifact;
    @Builder.Default
    private int parallelism = 1;
}
