package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

public class DescriptorCliClient implements CliClient {

    @Override
    public JobID submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        Command command = CommandFactory.buildSubmitCommand(deploymentTarget);
        deploymentTarget.apply(configuration);
        return command.submit(configuration, job);
    }

    @Override
    public JobID submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        Command command = CommandFactory.buildSubmitCommand(deploymentTarget);
        deploymentTarget.apply(configuration);
        return command.submit(configuration, job);
    }
}
