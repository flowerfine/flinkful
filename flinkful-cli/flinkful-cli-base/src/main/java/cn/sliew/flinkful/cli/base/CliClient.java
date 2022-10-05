package cn.sliew.flinkful.cli.base;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public interface CliClient {

    ClusterClient submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;

    ClusterClient submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;
}
