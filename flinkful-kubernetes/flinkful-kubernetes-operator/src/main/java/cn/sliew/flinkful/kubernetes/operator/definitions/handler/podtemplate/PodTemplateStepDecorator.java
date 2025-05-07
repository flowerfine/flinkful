package cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;

import java.util.List;

public interface PodTemplateStepDecorator {

    Pod decorate(Pod podTemplate);

    List<HasMetadata> buildRelatedResources();
}
