package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public interface SessionCommand {

    ClusterClient create(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration) throws Exception;
}
