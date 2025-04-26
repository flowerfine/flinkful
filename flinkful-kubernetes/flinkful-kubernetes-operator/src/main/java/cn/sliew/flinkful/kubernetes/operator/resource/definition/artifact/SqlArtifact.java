package cn.sliew.flinkful.kubernetes.operator.resource.definition.artifact;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.kubernetes.DockerImage;
import lombok.Data;

import java.util.List;

@Data
public class SqlArtifact implements Artifact {

    private String sql;
    private FlinkVersion flinkVersion;
    private DockerImage dockerImage;
    private List<String> additionalDependencies;

    @Override
    public FlinkJobType getType() {
        return FlinkJobType.JAR;
    }

    @Override
    public FlinkVersion getFlinkVersion() {
        return flinkVersion;
    }

    @Override
    public DockerImage getDockerImage() {
        return dockerImage;
    }
}
