package cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate;

import cn.sliew.carp.framework.kubernetes.util.ContainerUtil;
import cn.sliew.carp.framework.storage.config.OSSConfigProperties;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.util.ResourceNames;
import io.fabric8.kubernetes.api.model.*;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class FlinkFileSystemPluginStepDecorator extends AbstractPodTemplateStepDecorator {

    public static final String S3_ENDPOINT = "s3.endpoint";
    public static final String S3_ACCESS_KEY = "s3.access-key";
    public static final String S3_SECRET_KEY = "s3.secret-key";
    public static final String S3_PATH_STYLE_ACCESS = "s3.path.style.access";
    public static final String FS_ALLOWED_FALLBACK_FILESYSTEM = "fs.allowed-fallback-filesystems";

    public static final String OSS_ENDPOINT = "fs.oss.endpoint";
    public static final String OSS_ACCESS_KEY = "fs.oss.accessKeyId";
    public static final String OSS_SECRET_KEY = "fs.oss.accessKeySecret";

    private static final String FILE_SYSTEM_ENV_NAME = "ENABLE_BUILT_IN_PLUGINS";
    private static final String S3_FILE_SYSTEM_TEMPLATE = "flink-s3-fs-hadoop-%s.jar";
    private static final String OSS_FILE_SYSTEM_TEMPLATE = "flink-oss-fs-hadoop-%s.jar";
    private static final String AZURE_FILE_SYSTEM_TEMPLATE = "flink-azure-fs-hadoop-%s.jar";
    private static final String GS_FILE_SYSTEM_TEMPLATE = "flink-gs-fs-hadoop-%s.jar";

    private final FlinkVersion flinkVersion;
    private final StorageConfigProperties properties;

    @Override
    public Pod decorate(Pod podTemplate) {
        PodBuilder podBuilder = new PodBuilder(podTemplate);

        podBuilder.editOrNewMetadata()
                .withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = podBuilder.editOrNewSpec();

        ContainerUtil.getOrCreateContainer(spec, ResourceNames.FLINK_MAIN_CONTAINER_NAME)
                .addAllToEnv(buildEnableFileSystemEnv(flinkVersion))
                .endContainer();

        spec.endSpec();

        return podBuilder.build();
    }

    /**
     * 借助 flink 镜像的 ENABLE_BUILT_IN_PLUGINS 环境变量，自动启动对应的 filesystem 插件
     * filesystem 需要的访问信息，通过 flinkconfiguration 配置。部分参数也可以通过环境变量配置
     */
    private List<EnvVar> buildEnableFileSystemEnv(FlinkVersion flinkVersion) {
        if (properties.getS3() != null) {
            EnvVarBuilder fileSystemEnableEnv = new EnvVarBuilder();
            fileSystemEnableEnv.withName(FILE_SYSTEM_ENV_NAME);
            fileSystemEnableEnv.withValue(String.format(S3_FILE_SYSTEM_TEMPLATE, flinkVersion.getValue()));
            return Arrays.asList(fileSystemEnableEnv.build());
        }
        if (properties.getOss() != null) {
            OSSConfigProperties oss = properties.getOss();
            EnvVarBuilder fileSystemEnableEnv = new EnvVarBuilder();
            fileSystemEnableEnv.withName(FILE_SYSTEM_ENV_NAME);
            fileSystemEnableEnv.withValue(String.format(OSS_FILE_SYSTEM_TEMPLATE, flinkVersion.getValue()));
            // 展示 oss 如何通过环境变量设置授权信息，但是 endpoint 需要 flink-configuration
            // 同时添加如下配置：
            // fs.oss.credentials.provider: com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider
            EnvVarBuilder accessKeyEnv = new EnvVarBuilder();
            accessKeyEnv.withName(FileFetcherInitContainerStepDecorator.ENV_OSS_ACCESS);
            accessKeyEnv.withValue(oss.getAccessKey());
            EnvVarBuilder secretKeyEnv = new EnvVarBuilder();
            secretKeyEnv.withName(FileFetcherInitContainerStepDecorator.ENV_OSS_SECRET);
            secretKeyEnv.withValue(oss.getSecretKey());
            return Arrays.asList(fileSystemEnableEnv.build(), accessKeyEnv.build(), secretKeyEnv.build());
        }
        return Collections.emptyList();
    }
}
