package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DefaultSessionClusterResourceDefinition implements SessionClusterResourceDefinition {

    private final SessionCluster resource;
    private final List<HasMetadata> additionalResources;
}
