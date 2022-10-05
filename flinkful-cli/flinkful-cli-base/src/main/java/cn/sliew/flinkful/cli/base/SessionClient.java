package cn.sliew.flinkful.cli.base;

import cn.sliew.flinkful.cli.base.session.SessionCommand;
import cn.sliew.flinkful.cli.base.session.SessionFactory;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public class SessionClient {

    public static ClusterClient create(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case YARN_SESSION:
            case STANDALONE_SESSION:
                SessionCommand command = SessionFactory.buildSessionCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.create(deploymentTarget, flinkHome, configuration);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
