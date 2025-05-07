package cn.sliew.flinkful.kubernetes.operator.definitions.handler.job;

import cn.sliew.flinkful.kubernetes.common.artifact.JarArtifact;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType;
import cn.sliew.flinkful.kubernetes.common.upgrade.JobUpgradeMode;
import cn.sliew.flinkful.kubernetes.common.upgrade.SavepointUpgradeMode;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobState;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.UpgradeMode;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.JobSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;

@RequiredArgsConstructor
public class JarJobSpecProvider implements JobSpecProvider {

    private final DeploymentParameters parameters;

    @Override
    public FlinkJobType getJobType() {
        return FlinkJobType.JAR;
    }

    @Override
    public JobSpec getJobSpec() {
        JarArtifact jarArtifact = parameters.getArtifact().as(JarArtifact.class);
        JobUpgradeMode upgradeMode = jarArtifact.getUpgradeMode();
        return JobSpec.builder()
                .jarURI(jarArtifact.getJarUri())
                .entryClass(jarArtifact.getEntryClass())
                .args(jarArtifact.getMainArgs())
                .parallelism(parameters.getParallelism())
                .state(JobState.RUNNING)
                .upgradeMode(EnumUtils.getEnumIgnoreCase(UpgradeMode.class, upgradeMode.getType().getValue()))
                .allowNonRestoredState(upgradeMode.isAllowNonRestoredState())
                .initialSavepointPath(upgradeMode instanceof SavepointUpgradeMode savepointUpgradeMode ? savepointUpgradeMode.getSavepointPath() : null)
                .build();
    }
}
