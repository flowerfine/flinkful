package cn.sliew.flinkful.cli.base;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public interface CliClient {

    JobID submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;

    JobID submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;
}
