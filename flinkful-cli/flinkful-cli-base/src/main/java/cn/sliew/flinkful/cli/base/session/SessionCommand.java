package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;

public interface SessionCommand {

    ClusterClient create(DeploymentTarget deploymentTarget, Configuration configuration) throws Exception;
}
