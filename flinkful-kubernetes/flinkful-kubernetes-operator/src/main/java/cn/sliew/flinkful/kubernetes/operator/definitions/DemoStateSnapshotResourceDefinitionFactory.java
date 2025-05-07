package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.common.dict.operator.SavepointFormatType;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobKind;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkStateSnapshotSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkStateSnapshotMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkStateSnapshotSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.statesnapshot.FlinkStateSnapshot;
import cn.sliew.flinkful.kubernetes.operator.parameters.StateSnapshotParameters;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceLabels;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoStateSnapshotResourceDefinitionFactory implements StateSnapshotResourceDefinitionFactory {

    public static final UUID DEFAULT_STATE_SNAPSHOT_ID = UUID.fromString("4526da82-a73a-338b-d114-06f5b1d43b69");
    public static final String DEFAULT_STATE_SNAPSHOT_NAME = "test-state-snapshot" + DEFAULT_STATE_SNAPSHOT_ID;

    @Override
    public StateSnapshotResourceDefinition create() {
        StateSnapshotParameters parameters = StateSnapshotParameters.builder()
                .id(DEFAULT_STATE_SNAPSHOT_ID)
                .name(StringUtils.truncate(StringUtils.replace(DEFAULT_STATE_SNAPSHOT_NAME, "-", ""), 45))
                .namespace("default")
                .internalNamespace("default")
                .jobReferId(DemoDeploymentResourceDefinitionFactory.DEFAULT_DEPLOYMENT_ID)
                .jobReferKind(JobKind.FLINK_DEPLOYMENT)
                .jobReferName(StringUtils.truncate(StringUtils.replace(DemoDeploymentResourceDefinitionFactory.DEFAULT_DEPLOYMENT_NAME, "-", ""), 45))
                .disposeOnDelete(true)
                .formatType(SavepointFormatType.CANONICAL)
                .build();

        FlinkStateSnapshotMetadataProvider flinkStateSnapshotMetadataProvider = getFlinkStateSnapshotMetadataProvider(parameters);
        FlinkStateSnapshotSpecProvider flinkStateSnapshotSpecProvider = getFlinkStateSnapshotSpecProvider(parameters);
        FlinkStateSnapshot stateSnapshot = FlinkStateSnapshot.builder()
                .metadata(flinkStateSnapshotMetadataProvider.getMetadata())
                .spec(flinkStateSnapshotSpecProvider.getSpec())
                .build();
        return new DefaultStateSnapshotResourceDefinition(stateSnapshot);
    }

    private FlinkStateSnapshotMetadataProvider getFlinkStateSnapshotMetadataProvider(StateSnapshotParameters parameters) {
        return () -> {
            return FlinkStateSnapshot.FlinkStateSnapshotMetadata.builder()
                    .name(parameters.getName())
                    .namespace(parameters.getNamespace())
                    .labels(ResourceLabels.getStateSnapshotLabels(parameters))
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkStateSnapshotSpecProvider getFlinkStateSnapshotSpecProvider(StateSnapshotParameters parameters) {
        return new DefaultFlinkStateSnapshotSpecProvider(parameters);
    }
}
