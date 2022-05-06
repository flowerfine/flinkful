package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.submit.CommandFactory;
import cn.sliew.flinkful.cli.descriptor.submit.SubmitCommand;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

public class DescriptorCliClient implements CliClient {

    @Override
    public JobID submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case YARN_SESSION:
            case YARN_PER_JOB:
            case STANDALONE_SESSION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public JobID submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_APPLICATION:
            case YARN_APPLICATION:
            case STANDALONE_APPLICATION:
                SubmitCommand command = CommandFactory.buildSubmitCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.submit(configuration, job);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
