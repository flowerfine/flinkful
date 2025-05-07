package cn.sliew.flinkful.kubernetes.operator.definitions.handler.job;

import cn.sliew.flinkful.kubernetes.common.artifact.JarArtifact;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobSpec;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobState;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.UpgradeMode;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.JobSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import lombok.RequiredArgsConstructor;

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
        return JobSpec.builder()
                .jarURI(jarArtifact.getJarUri())
                .entryClass(jarArtifact.getEntryClass())
                .args(jarArtifact.getMainArgs())
                .parallelism(parameters.getParallelism())
                .state(JobState.RUNNING)
                .upgradeMode(UpgradeMode.STATELESS)
                .build();
    }
}
