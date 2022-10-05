package cn.sliew.flinkful.cli.frontend.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.*;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.YarnClusterDescriptor;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class YarnApplicationExample {

    public static void main(String[] args) throws Exception {
        CliClient client = Util.buildCliClient();
        client.submitApplication(DeploymentTarget.YARN_APPLICATION, null, buildConfiguration(), Util.buildJarJob());
    }

    /**
     * 以 application 模式提交到 YARN 时，同样需要设置 flink 任务需要的内存资源。
     * <p>
     * application 模式下，kubernetes 要求将任务 jar 和依赖、包括 flink 本身都打进镜像，
     * 在 YARN 中，有 2 种方式可以提供任务 jar 和依赖、包括 flink。
     * 1. {@link YarnConfigOptions#PROVIDED_LIB_DIRS}。提前将任务 jar 和依赖、包括 flink
     * 上传至 hdfs，YARN 运行 flink 任务时会自动从 hdfs 中加载。
     * 2. {@link YarnClusterDescriptor#setLocalJarPath(Path)} 和 {@link YarnClusterDescriptor#addShipFiles(List)}。
     * 参考 {@link YarnFlinkUtil#addJarFiles}
     * 因为 flinkful-cli-frontend 不会使用到 {@link YarnClusterDescriptor}，所以只能使用 {@link YarnConfigOptions#PROVIDED_LIB_DIRS}。
     */
    static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        configuration.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);

        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://hadoop:9000/flink/1.13.6"}));
        configuration.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://hadoop:9000/flink/1.13.6/flink-dist_2.11-1.13.6.jar");

        URL exampleUrl = new File(FlinkExamples.EXAMPLE_JAR).toURL();
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(exampleUrl), Object::toString);
        return configuration;
    }


}
