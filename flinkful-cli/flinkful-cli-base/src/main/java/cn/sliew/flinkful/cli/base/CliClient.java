package cn.sliew.flinkful.cli.base;

import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.configuration.Configuration;

public interface CliClient {

    void submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception;

    void submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception;

}
