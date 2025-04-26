package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.submit.CommandFactory;
import cn.sliew.flinkful.cli.descriptor.submit.SubmitCommand;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public class DescriptorCliClient implements CliClient {

    @Override
    public ClusterClient submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case STANDALONE_SESSION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(flinkHome, configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public ClusterClient submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_APPLICATION:
            case STANDALONE_APPLICATION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(flinkHome, configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
