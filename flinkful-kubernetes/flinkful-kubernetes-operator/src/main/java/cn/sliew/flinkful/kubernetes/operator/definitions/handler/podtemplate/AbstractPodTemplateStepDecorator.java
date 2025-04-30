package cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPodTemplateStepDecorator implements PodTemplateStepDecorator {

    @Override
    public Pod decorate(Pod podTemplate) {
        return podTemplate;
    }

    @Override
    public List<HasMetadata> buildRelatedResources() throws IOException {
        return Collections.emptyList();
    }
}
