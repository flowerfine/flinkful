package cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec;

import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobManagerSpec;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.List;

public interface JobManagerSpecStepDecorator {

    JobManagerSpec decorate(JobManagerSpec spec);

    List<HasMetadata> buildRelatedResources();
}
