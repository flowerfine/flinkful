package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkDeploymentSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkDeploymentMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkDeploymentSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoDeploymentResourceDefinitionFactory implements DeploymentResourceDefinitionFactory {

    private static final UUID DEFAULT_DEPLOYMENT_ID = UUID.fromString("2d9c4deb-f3ad-d124-fa3b-41127a14ccbe");
    private static final String DEFAULT_DEPLOYMENT_NAME = "test-deployment";

    @Override
    public DeploymentResourceDefinition create() {
        StorageConfigProperties properties = new StorageConfigProperties();
        properties.setType("s3");
        S3ConfigProperties s3 = new S3ConfigProperties();
        s3.setBucket("carp");
        s3.setEndpoint("http://127.0.0.1:9000");
        s3.setAccessKey("admin");
        s3.setSecretKey("password");
        properties.setS3(s3);
        Map<String, String> labels = Map.of("system", "flinkful",
                "internalNamespace", "default",
                "app", "flink",
                "instance", "instance-1",
                "component", "deployment",
                "deploymentId", DEFAULT_DEPLOYMENT_ID.toString(),
                "deploymentName", DEFAULT_DEPLOYMENT_NAME);

        DeploymentParameters parameters = DeploymentParameters.builder()
                .id(DEFAULT_DEPLOYMENT_ID)
                .flinkVersion(FlinkVersion.V_1_18_1)
                .properties(properties)
                .labels(labels)
                .build();

        FlinkDeploymentMetadataProvider flinkDeploymentMetadataProvider = getFlinkDeploymentMetadataProvider(parameters);
        FlinkDeploymentSpecProvider flinkDeploymentSpecProvider = getFlinkDeploymentSpecProvider(parameters);
        Deployment deployment = Deployment.builder()
                .metadata(flinkDeploymentMetadataProvider.getMetadata())
                .spec(flinkDeploymentSpecProvider.getSpec())
                .build();
        List<HasMetadata> additionalResources = flinkDeploymentSpecProvider.getAdditionalResources();
        return new DefaultDeploymentResourceDefinition(deployment, additionalResources);
    }

    private FlinkDeploymentMetadataProvider getFlinkDeploymentMetadataProvider(DeploymentParameters parameters) {
        return () -> {
            return Deployment.DeploymentMetadata.builder()
                    .name(DEFAULT_DEPLOYMENT_ID.toString())
                    .namespace("default")
                    .labels(parameters.getLabels())
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkDeploymentSpecProvider getFlinkDeploymentSpecProvider(DeploymentParameters parameters) {
        return new DefaultFlinkDeploymentSpecProvider(parameters);
    }
}
