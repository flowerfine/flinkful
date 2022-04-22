package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.FlinkUtil;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;

public class RestClusterClientCommand implements Command {

    @Override
    public JobID submit(Configuration configuration, PackageJarJob job) throws Exception {
        RestClusterClient<StandaloneClusterId> client = createClusterClient(configuration);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return client.submitJob(jobGraph).get();
    }

    private RestClusterClient<StandaloneClusterId> createClusterClient(Configuration configuration) throws Exception {
        configuration.setString(JobManagerOptions.ADDRESS, "127.0.0.1");
        configuration.setInteger(RestOptions.PORT, 8081);
        configuration.setInteger(RestOptions.RETRY_MAX_ATTEMPTS, 3);
        RestClusterClient<StandaloneClusterId> client = new RestClusterClient<>(configuration, StandaloneClusterId.getInstance());
        return client;
    }
}
