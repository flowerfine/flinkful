package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class StandaloneSession2Example {

    //    private static String mysqlPath = "/Users/wangqi/Documents/software/repository/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar";
    private static String mysqlPath = "/Library/Maven/repository/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar";

    private static String seatunnelHome = "/Users/wangqi/Downloads/apache-seatunnel-incubating-2.0.5-SNAPSHOT";
    private static String seatunnelPath = seatunnelHome + "/lib/seatunnel-core-flink.jar";


    public static void main(String[] args) throws Exception {
        CliClient client = new DescriptorCliClient();
        client.submit(DeploymentTarget.STANDALONE_SESSION, buildConfiguration(), buildJarJob());
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
        URL mysqlURL = new File(mysqlPath).toURL();
        List<URL> jars = Collections.singletonList(mysqlURL);
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        return configuration;
    }

    private static PackageJarJob buildJarJob() {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath("file://" + seatunnelPath);
        job.setEntryPointClass("org.apache.seatunnel.SeatunnelFlink");
        URL resource = StandaloneSession2Example.class.getClassLoader().getResource("flink_jdbc_file.conf");
        job.setProgramArgs(new String[]{"--config", resource.getPath()});
        job.setClasspaths(Collections.emptyList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
