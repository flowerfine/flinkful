package cn.sliew.flinkful.kubernetes.common.artifact;

import cn.sliew.carp.framework.common.jackson.polymorphic.Polymorphic;
import cn.sliew.carp.framework.kubernetes.model.ContainerImage;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
public class UnknownArtifact implements Artifact, Polymorphic.Unknown {

    @Override
    public FlinkJobType getType() {
        return FlinkJobType.UNKNOWN;
    }

    @Override
    public FlinkVersion getFlinkVersion() {
        return null;
    }

    @Override
    public ContainerImage getContainerImage() {
        return null;
    }
}