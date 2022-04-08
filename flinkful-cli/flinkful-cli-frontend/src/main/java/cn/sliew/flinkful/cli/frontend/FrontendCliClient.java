package cn.sliew.flinkful.cli.frontend;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.ClientUtils;
import org.apache.flink.client.cli.ApplicationDeployer;
import org.apache.flink.client.cli.CliFrontend;
import org.apache.flink.client.deployment.ClusterClientServiceLoader;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.deployment.application.cli.ApplicationClusterDeployer;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;

public class FrontendCliClient implements CliClient {

    private final ClusterClientServiceLoader clusterClientServiceLoader = new DefaultClusterClientServiceLoader();
    private final ApplicationDeployer deployer = new ApplicationClusterDeployer(clusterClientServiceLoader);

    private final PipelineExecutorServiceLoader pipelineExecutorServiceLoader = new DefaultExecutorServiceLoader();

    /**
     * @see CliFrontend#run(String[])
     */
    @Override
    public void submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        try (PackagedProgram program = FlinkUtil.buildProgram(configuration, job)) {
            ClientUtils.executeProgram(pipelineExecutorServiceLoader, configuration, program, false, false);
        }
    }

    /**
     * @see CliFrontend#runApplication(String[])
     */
    @Override
    public void submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        deployer.run(configuration, applicationConfiguration);
    }
}
