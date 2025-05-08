package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.flinkful.kubernetes.operator.util.ResourceNames;
import lombok.RequiredArgsConstructor;
import org.apache.flink.configuration.CoreOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FileFetcherJavaEnvsStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

//        flinkConfiguration.put(CoreOptions.FLINK_JM_JVM_OPTIONS.key(), "-classpath " + ResourceNames.FLINKFUL_USRLIB_DIRECTORY_ENV);

        return flinkConfiguration;
    }
}
