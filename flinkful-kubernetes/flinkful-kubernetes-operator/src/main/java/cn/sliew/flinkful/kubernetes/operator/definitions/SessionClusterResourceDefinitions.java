package cn.sliew.flinkful.kubernetes.operator.definitions;

import cn.sliew.flinkful.kubernetes.operator.entity.sessioncluster.SessionCluster;
import io.fabric8.kubernetes.api.model.Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SessionClusterResourceDefinitions {

    private final SessionCluster sessionCluster;

    public Service getJobManagerService() {

    }
}
