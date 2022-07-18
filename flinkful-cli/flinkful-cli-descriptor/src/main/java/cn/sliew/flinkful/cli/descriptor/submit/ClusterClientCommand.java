package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.ClusterRetrieveException;
import org.apache.flink.client.deployment.StandaloneClusterDescriptor;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;

import java.nio.file.Path;

public class ClusterClientCommand implements SubmitCommand {

    /**
     * Standalone 模式下可以使用 jobmanager 的地址或者使用 rest 地址。
     * 对于 yarn session 和 native kubernetes session 模式下，jobmanager 的地址由 yarn 或 native kubernetes 下处理，
     * 推荐使用 rest 地址。
     * todo jobmanager 地址 和 webInterfaceUrl 的优先级问题？
     */
    @Override
    public JobID submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        ClusterClientFactory<StandaloneClusterId> factory = FlinkUtil.createClientFactory(configuration);
        ClusterClient<StandaloneClusterId> client = createClusterClient(configuration, factory);
        PackagedProgram program = FlinkUtil.buildProgram(configuration, job);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, configuration, 1, false);
        return client.submitJob(jobGraph).get();
    }

    private ClusterClient<StandaloneClusterId> createClusterClient(Configuration configuration,
                                                                   ClusterClientFactory<StandaloneClusterId> factory) throws ClusterRetrieveException {
        StandaloneClusterId clusterId = factory.getClusterId(configuration);
        StandaloneClusterDescriptor clusterDescriptor = (StandaloneClusterDescriptor) factory.createClusterDescriptor(configuration);
        return clusterDescriptor.retrieve(clusterId).getClusterClient();
    }
}
