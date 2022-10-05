package cn.sliew.flinkful.cli.frontend;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.client.ClientUtils;
import cn.sliew.flinkful.shade.org.apache.flink.client.cli.ApplicationDeployer;
import cn.sliew.flinkful.shade.org.apache.flink.client.cli.CliFrontend;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.ClusterClientServiceLoader;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.application.ApplicationConfiguration;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.application.cli.ApplicationClusterDeployer;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import cn.sliew.flinkful.shade.org.apache.flink.core.execution.PipelineExecutorServiceLoader;

import java.nio.file.Path;

public class FrontendCliClient implements CliClient {

    private final ClusterClientServiceLoader clusterClientServiceLoader = new DefaultClusterClientServiceLoader();
    private final ApplicationDeployer deployer = new ApplicationClusterDeployer(clusterClientServiceLoader);

    private final PipelineExecutorServiceLoader pipelineExecutorServiceLoader = new DefaultExecutorServiceLoader();

    /**
     * @see CliFrontend#run(String[])
     */
    @Override
    public ClusterClient submit(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        try (PackagedProgram program = FlinkUtil.buildProgram(configuration, job)) {
            ClientUtils.executeProgram(pipelineExecutorServiceLoader, configuration, program, false, false);
        }
        return null;
    }

    /**
     * @see CliFrontend#runApplication(String[])
     */
    @Override
    public ClusterClient submitApplication(DeploymentTarget deploymentTarget, Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        deployer.run(configuration, applicationConfiguration);
        return null;
    }
}
