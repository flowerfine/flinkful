package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.carp.framework.storage.config.S3ConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.DefaultFlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterMetadataProvider;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.FlinkSessionClusterSpecProvider;
import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import cn.sliew.flinkful.kubernetes.operator.parameters.SessionClusterParameters;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceLabels;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DemoSessionClusterResourceDefinitionFactory implements SessionClusterResourceDefinitionFactory {

    public static final UUID DEFAULT_SESSION_CLUSTER_ID = UUID.fromString("dac4ca57-6dd2-3168-3d27-7e23a91c85d7");
    public static final String DEFAULT_SESSION_CLUSTER_NAME = "test-session-cluster" + DEFAULT_SESSION_CLUSTER_ID;

    @Override
    public SessionClusterResourceDefinition create() {
        StorageConfigProperties properties = new StorageConfigProperties();
        properties.setType("s3");
        S3ConfigProperties s3 = new S3ConfigProperties();
        s3.setBucket("carp");
        s3.setEndpoint("http://127.0.0.1:9000");
        s3.setAccessKey("admin");
        s3.setSecretKey("password");
        properties.setS3(s3);

        SessionClusterParameters parameters = SessionClusterParameters.builder()
                .id(DEFAULT_SESSION_CLUSTER_ID)
                .name(StringUtils.truncate(StringUtils.replace(DEFAULT_SESSION_CLUSTER_NAME, "-", ""), 45))
                .namespace("default")
                .internalNamespace("default")
                .flinkVersion(FlinkVersion.V_1_18_1)
                .properties(properties)
                .build();

        FlinkSessionClusterMetadataProvider flinkSessionClusterMetadataProvider = getFlinkSessionClusterMetadataProvider(parameters);
        FlinkSessionClusterSpecProvider flinkSessionClusterSpecProvider = getFlinkSessionClusterSpecProvider(parameters);
        SessionCluster sessionCluster = SessionCluster.builder()
                .internalMetadata(flinkSessionClusterMetadataProvider.getMetadata())
                .spec(flinkSessionClusterSpecProvider.getSpec())
                .build();
        List<HasMetadata> additionalResources = flinkSessionClusterSpecProvider.getAdditionalResources();
        return new DefaultSessionClusterResourceDefinition(sessionCluster, additionalResources);
    }

    private FlinkSessionClusterMetadataProvider getFlinkSessionClusterMetadataProvider(SessionClusterParameters parameters) {
        return () -> {
            return SessionCluster.SessionClusterMetadata.builder()
                    .name(parameters.getName())
                    .namespace(parameters.getNamespace())
                    .labels(ResourceLabels.getSessionClusterLabels(parameters))
                    .annotations(Collections.emptyMap())
                    .build();
        };
    }

    private FlinkSessionClusterSpecProvider getFlinkSessionClusterSpecProvider(SessionClusterParameters parameters) {
        return new DefaultFlinkSessionClusterSpecProvider(parameters);
    }
}
