package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.StandaloneClusterId;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgramUtils;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.rest.RestClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobgraph.JobGraph;

import java.nio.file.Path;

public class RestClusterClientCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        RestClusterClient<StandaloneClusterId> client = createClusterClient(configuration);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, job.getParallelism(), false);
        client.submitJob(jobGraph).get();
        return client;
    }

    private RestClusterClient<StandaloneClusterId> createClusterClient(Configuration configuration) throws Exception {
        return new RestClusterClient<>(configuration, StandaloneClusterId.getInstance());
    }
}
