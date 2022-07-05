package cn.sliew.flinkful.cli.base.util;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import org.apache.flink.client.deployment.ClusterClientFactory;
import org.apache.flink.client.deployment.ClusterDescriptor;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

public enum FlinkUtil {
    ;

    public static String getHadoopHome() {
        return System.getenv("HADOOP_HOME");
    }

    public static String getFlinkHome() {
        return System.getenv("FLINK_HOME");
    }

    public static String getFlinkConfDir() {
        return getFlinkHome() + File.separator + "conf";
    }

    public static String getFlinkPluginsDir() {
        return getFlinkHome() + File.separator + "plugins";
    }

    public static String getFlinkLibDir() {
        return getFlinkHome() + File.separator + "lib";
    }

    public static String getFlinkExamplesDir() {
        return getFlinkHome() + File.separator + "examples";
    }

    public static String getFlinkDistJar() {
        return getFlinkLibDir() + File.separator + "flink-dist_2.11-1.13.6.jar";
    }

    public static Configuration loadConfiguration() {
        return GlobalConfiguration.loadConfiguration(getFlinkConfDir(), new Configuration());
    }

    public static PackagedProgram buildProgram(Configuration configuration, PackageJarJob job) throws FileNotFoundException, ProgramInvocationException, URISyntaxException {
        String jarFilePath = job.getJarFilePath();
        File jarFile = jarFilePath != null ? getJarFile(jarFilePath) : null;
        return PackagedProgram.newBuilder()
                .setJarFile(jarFile)
                .setUserClassPaths(job.getClasspaths())
                .setEntryPointClassName(job.getEntryPointClass())
                .setConfiguration(configuration)
                .setSavepointRestoreSettings(job.getSavepointSettings())
                .setArguments(job.getProgramArgs())
                .build();
    }

    /**
     * Gets the JAR file from the path.
     *
     * @param jarFilePath The path of JAR file
     * @throws FileNotFoundException The JAR file does not exist.
     */
    private static File getJarFile(String jarFilePath) throws FileNotFoundException, URISyntaxException {
        File jarFile = new File(new URI(jarFilePath));
        // Check if JAR file exists
        if (!jarFile.exists()) {
            throw new FileNotFoundException("JAR file does not exist: " + jarFile);
        } else if (!jarFile.isFile()) {
            throw new FileNotFoundException("JAR file is not a file: " + jarFile);
        }
        return jarFile;
    }

    public static ClusterClient retrieve(Configuration configuration) throws Exception {
        ClusterClientFactory factory = FlinkUtil.createClientFactory(configuration);
        ClusterDescriptor clusterDescriptor = FlinkUtil.createClusterDescriptor(factory, configuration);

        Object clusterId = factory.getClusterId(configuration);
        return clusterDescriptor.retrieve(clusterId).getClusterClient();
    }

    public static ClusterDescriptor createClusterDescriptor(Configuration config) {
        final ClusterClientFactory factory = createClientFactory(config);
        return factory.createClusterDescriptor(config);
    }

    public static ClusterDescriptor createClusterDescriptor(ClusterClientFactory factory, Configuration config) {
        return factory.createClusterDescriptor(config);
    }

    public static ClusterClientFactory createClientFactory(Configuration config) {
        DefaultClusterClientServiceLoader serviceLoader = new DefaultClusterClientServiceLoader();
        return serviceLoader.getClusterClientFactory(config);
    }

    public static ClusterSpecification createClusterSpecification(Configuration configuration) {
        MemorySize jobManagerMem = configuration.getOptional(JobManagerOptions.TOTAL_PROCESS_MEMORY).orElse(MemorySize.ofMebiBytes(1024));
        MemorySize taskManagerMem = configuration.getOptional(TaskManagerOptions.TOTAL_PROCESS_MEMORY).orElse(MemorySize.ofMebiBytes(1024));
        Integer slots = configuration.get(TaskManagerOptions.NUM_TASK_SLOTS);
        return new ClusterSpecification.ClusterSpecificationBuilder()
                .setMasterMemoryMB(jobManagerMem.getMebiBytes())
                .setTaskManagerMemoryMB(taskManagerMem.getMebiBytes())
                .setSlotsPerTaskManager(slots)
                .createClusterSpecification();
    }

}
