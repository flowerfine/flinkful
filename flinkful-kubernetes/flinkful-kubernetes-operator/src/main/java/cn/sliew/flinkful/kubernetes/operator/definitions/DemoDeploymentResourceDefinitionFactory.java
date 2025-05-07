package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.common.dict.k8s.CarpK8sImagePullPolicy;
import cn.sliew.carp.framework.kubernetes.model.ContainerImage;
import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.artifact.JarArtifact;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.common.upgrade.SavepointUpgradeMode;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkDeploymentSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkDeploymentMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkDeploymentSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.deployment.Deployment;
import cn.sliew.flinkful.kubernetes.operator.parameters.DeploymentParameters;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceLabels;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoDeploymentResourceDefinitionFactory implements DeploymentResourceDefinitionFactory {

    static final UUID DEFAULT_DEPLOYMENT_ID = UUID.fromString("2d9c4deb-f3ad-d124-fa3b-41127a14ccbe");
    static final String DEFAULT_DEPLOYMENT_NAME = "test-deployment" + DEFAULT_DEPLOYMENT_ID;

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

        DeploymentParameters parameters = DeploymentParameters.builder()
                .id(DEFAULT_DEPLOYMENT_ID)
                .name(StringUtils.truncate(StringUtils.replace(DEFAULT_DEPLOYMENT_NAME, "-", ""), 45))
                .namespace("default")
                .internalNamespace("default")
                .properties(properties)
                .artifact(JarArtifact.builder()
                        .jarUri("https://repo1.maven.org/maven2/org/apache/flink/flink-examples-streaming/1.19.0/flink-examples-streaming-1.19.0-TopSpeedWindowing.jar")
                        .entryClass("org.apache.flink.streaming.examples.windowing.TopSpeedWindowing")
                        .mainArgs(new String[]{"--env", "prod"})
                        .flinkVersion(FlinkVersion.V_1_19_0)
                        .containerImage(ContainerImage.builder()
                                .imagePullPolicy(CarpK8sImagePullPolicy.IF_NOT_PRESENT)
                                .repository("flink")
                                .tag("1.19.0-scala_2.12-java8")
                                .build())
                        .upgradeMode(SavepointUpgradeMode.builder()
                                .savepointPath("s3a://carp/flinkful/jobs/2d9c4deb-f3ad-d124-fa3b-41127a14ccbe/savepoints/savepoint-cc71a5-c29faf0f0cf3")
                                .build())
                        .build())
                .parallelism(1)
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
                    .name(parameters.getName())
                    .namespace(parameters.getNamespace())
                    .labels(ResourceLabels.getDeploymentLabels(parameters))
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkDeploymentSpecProvider getFlinkDeploymentSpecProvider(DeploymentParameters parameters) {
        return new DefaultFlinkDeploymentSpecProvider(parameters);
    }
}
