package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.JobSpec;
import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.Collections;
import java.util.List;

public interface JobSpecProvider {

    FlinkJobType getJobType();

    JobSpec getJobSpec();

    default List<HasMetadata> getAdditionalResources() {
        return Collections.emptyList();
    }
}
