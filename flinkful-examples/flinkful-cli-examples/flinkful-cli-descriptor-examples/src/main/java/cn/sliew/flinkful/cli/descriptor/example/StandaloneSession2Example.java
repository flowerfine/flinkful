package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StandaloneSession2Example {

    private static String SEATUNNEL_HOME = "/Users/wangqi/Documents/software/seatunnel/apache-seatunnel-incubating-2.2.0-beta";
    private static String CORE_JAR = SEATUNNEL_HOME + "/lib/seatunnel-flink-starter.jar";
    private static String FAKE_CONNECTOR_JAR = SEATUNNEL_HOME + "/connectors/seatunnel/connector-fake-2.2.0-beta.jar";
    private static String CONSOLE_CONNECTOR_JAR = SEATUNNEL_HOME + "/connectors/seatunnel/connector-console-2.2.0-beta.jar";

    public static void main(String[] args) throws Exception {
        CliClient client = new DescriptorCliClient();
        final java.nio.file.Path flinkHome = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.13.6");
        client.submit(DeploymentTarget.STANDALONE_SESSION, flinkHome, buildConfiguration(), buildJarJob());
    }

    /**
     * 通过 {@link PipelineOptions#JARS} 将任务 jar 包和对应的依赖都可以一起传到 JobManager。
     * 通过这种方式，可以避免在 JobManager 手动添加 seatunnel-core-flink.jar 或 mysql-connector-java.jar
     */
    private static Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = FlinkExamples.loadConfiguration();
        configuration.setString(JobManagerOptions.ADDRESS, "localhost");
        configuration.setInteger(JobManagerOptions.PORT, 6123);
        configuration.setInteger(RestOptions.PORT, 8081);
        URL fakeConnector = new File(FAKE_CONNECTOR_JAR).toURL();
        URL consoleConnector = new File(CONSOLE_CONNECTOR_JAR).toURL();
        List<URL> jars = Arrays.asList(fakeConnector, consoleConnector);
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        return configuration;
    }

    private static PackageJarJob buildJarJob() {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath("file://" + CORE_JAR);
        job.setEntryPointClass("org.apache.seatunnel.core.starter.flink.SeatunnelFlink");
        URL resource = StandaloneSession2Example.class.getClassLoader().getResource("fake_console.json");
        job.setProgramArgs(new String[]{"--config", resource.getPath()});
        job.setClasspaths(Collections.emptyList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
