package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.carp.framework.storage.FileSystemType;
import cn.sliew.carp.framework.storage.config.StorageConfigProperties;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class FlinkStateStorageStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    private final StorageConfigProperties properties;
    private final UUID id;

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

        addStateStorageConfigOption(flinkConfiguration);

        return flinkConfiguration;
    }

    private void addStateStorageConfigOption(Map<String, String> configuration) {
        String schemaAndPath = getSchemaAndPath();
        configuration.put("state.checkpoints.dir", getCheckpointPath(schemaAndPath, id));
        configuration.put("state.savepoints.dir", getSavepointPath(schemaAndPath, id));
        configuration.put("high-availability.storageDir", getHaPath(schemaAndPath, id));
        configuration.put("high-availability", "org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory");
    }

    private String getSchemaAndPath() {
        FileSystemType fileSystemType = FileSystemType.of(properties.getType());
        if (properties.getS3() != null) {
            return String.format("%s%s", fileSystemType.getSchema(), properties.getS3().getBucket());
        }
        if (properties.getOss() != null) {
            return String.format("%s%s", fileSystemType.getSchema(), properties.getOss().getBucket());
        }
        if (properties.getHdfs() != null) {
            return String.format("%s", fileSystemType.getSchema());
        }
        if (properties.getLocal() != null) {
            return String.format("%s%s", fileSystemType.getSchema(), properties.getLocal().getPath());
        }
        return String.format("%s", fileSystemType.getSchema());
    }

    private static String getCheckpointPath(String schema, UUID uuid) {
        return String.format("%s/flinkful/jobs/%s/checkpoints/", schema, uuid.toString());
    }

    private static String getSavepointPath(String schema, UUID uuid) {
        return String.format("%s/flinkful/jobs/%s/savepoints/", schema, uuid.toString());
    }

    private static String getHaPath(String schema, UUID uuid) {
        return String.format("%s/flinkful/jobs/%s/ha/", schema, uuid.toString());
    }
}
