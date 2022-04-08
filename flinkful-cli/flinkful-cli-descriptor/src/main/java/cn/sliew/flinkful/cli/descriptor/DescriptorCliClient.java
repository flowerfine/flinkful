package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.configuration.Configuration;

public class DescriptorCliClient implements CliClient {

    @Override
    public void submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        Command command = CommandFactory.buildSubmitCommand(deploymentTarget);
        deploymentTarget.apply(configuration);
        command.submit(configuration, job);
    }

    @Override
    public void submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        Command command = CommandFactory.buildSubmitCommand(deploymentTarget);
        deploymentTarget.apply(configuration);
        command.submit(configuration, job);
    }
}
