package cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobManagerSpec;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.Collections;
import java.util.List;

public abstract class AbstractJobManagerSpecStepDecorator implements JobManagerSpecStepDecorator {

    @Override
    public JobManagerSpec decorate(JobManagerSpec spec) {
        return spec;
    }

    @Override
    public List<HasMetadata> buildRelatedResources() {
        return Collections.emptyList();
    }
}
