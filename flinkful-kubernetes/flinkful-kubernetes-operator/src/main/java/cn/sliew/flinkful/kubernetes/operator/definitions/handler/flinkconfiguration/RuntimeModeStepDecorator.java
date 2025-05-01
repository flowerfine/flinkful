package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.flinkful.kubernetes.common.dict.FlinkRuntimeExecutionMode;
import lombok.RequiredArgsConstructor;
import org.apache.flink.configuration.ExecutionOptions;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RuntimeModeStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    private final FlinkRuntimeExecutionMode executionMode;

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

        flinkConfiguration.put(ExecutionOptions.RUNTIME_MODE.key(), executionMode.getValue());

        return flinkConfiguration;
    }
}
