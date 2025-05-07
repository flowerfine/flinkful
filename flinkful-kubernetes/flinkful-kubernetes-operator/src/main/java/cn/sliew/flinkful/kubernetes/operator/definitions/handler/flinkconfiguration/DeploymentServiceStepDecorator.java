package cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration;

import cn.sliew.flinkful.kubernetes.operator.util.FlinkServiceUtil;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DeploymentServiceStepDecorator extends AbstractFlinkConfigurationStepDecorator {

    @Override
    public Map<String, String> decorate(Map<String, String> parameters) {
        Map<String, String> flinkConfiguration = new HashMap<>(parameters);

        flinkConfiguration.putAll(FlinkServiceUtil.addNodePortService());

        return flinkConfiguration;
    }
}
