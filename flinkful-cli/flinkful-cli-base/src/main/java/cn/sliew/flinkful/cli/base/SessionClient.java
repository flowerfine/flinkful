package cn.sliew.flinkful.cli.base;

import cn.sliew.flinkful.cli.base.session.SessionCommand;
import cn.sliew.flinkful.cli.base.session.SessionFactory;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;

public class SessionClient {

    public static ClusterClient create(DeploymentTarget deploymentTarget, Configuration configuration) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case YARN_SESSION:
            case STANDALONE_SESSION:
                SessionCommand command = SessionFactory.buildSessionCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.create(deploymentTarget, configuration);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
