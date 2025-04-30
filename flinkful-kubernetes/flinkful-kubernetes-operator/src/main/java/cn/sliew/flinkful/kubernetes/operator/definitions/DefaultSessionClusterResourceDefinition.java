package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DefaultSessionClusterResourceDefinition implements SessionClusterResourceDefinition {

    private final SessionCluster sessionCluster;
}
