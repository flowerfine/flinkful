package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.executors.LocalExecutor;
import org.apache.flink.client.program.MiniClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;

import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class MiniClusterCommand implements SubmitCommand {

    @Override
    public JobID submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        MiniCluster cluster = createCluster(configuration);
        MiniClusterClient client = createClusterClient(cluster, configuration);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return client.submitJob(jobGraph).get();
    }

    private MiniCluster createCluster(Configuration config) throws Exception {
        config.setInteger(JobManagerOptions.PORT, JobManagerOptions.PORT.defaultValue());
        config.setInteger(ConfigConstants.LOCAL_NUMBER_TASK_MANAGER, ConfigConstants.DEFAULT_LOCAL_NUMBER_TASK_MANAGER);
        config.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, TaskManagerOptions.NUM_TASK_SLOTS.defaultValue());
//        config.set(DeploymentOptions.JOB_LISTENERS, Arrays.asList("cn.sliew.flink.demo.submit.listener.DemoJobListener"));
        MiniClusterConfiguration miniClusterConfig = new MiniClusterConfiguration.Builder()
                .setConfiguration(config)
                .build();
        MiniCluster cluster = new MiniCluster(miniClusterConfig);
        cluster.start();
        return cluster;
    }

    private MiniClusterClient createClusterClient(MiniCluster cluster, Configuration config) throws ExecutionException, InterruptedException {
        URI address = cluster.getRestAddress().get();

        config.setString(JobManagerOptions.ADDRESS, address.getHost());
        config.setInteger(JobManagerOptions.PORT, address.getPort());
        config.setString(RestOptions.ADDRESS, address.getHost());
        config.setInteger(RestOptions.PORT, address.getPort());
        config.setString(DeploymentOptions.TARGET, LocalExecutor.NAME);

        return new MiniClusterClient(config, cluster);
    }
}
