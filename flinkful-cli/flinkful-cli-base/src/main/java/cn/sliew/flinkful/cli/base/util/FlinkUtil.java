package cn.sliew.flinkful.cli.base.util;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.*;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.PackagedProgram;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ProgramInvocationException;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.GlobalConfiguration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.JobManagerOptions;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.TaskManagerOptions;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.client.JobStatusMessage;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.clusterframework.TaskExecutorProcessUtils;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.concurrent.FutureUtils;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobmanager.JobManagerProcessUtils;
import cn.sliew.flinkful.shade.org.apache.flink.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public enum FlinkUtil {
    ;

    public static Path getHadoopHome() {
        final String hadoopHome = System.getenv("HADOOP_HOME");
        if (StringUtils.isNullOrWhitespaceOnly(hadoopHome)) {
            return null;
        }
        return Paths.get(hadoopHome);
    }

    public static Path getFlinkHomeEnv() {
        final String flinkHome = System.getenv("FLINK_HOME");
        if (StringUtils.isNullOrWhitespaceOnly(flinkHome)) {
            return null;
        }
        return Paths.get(flinkHome);
    }

    public static Path getFlinkConfDirEnv() {
        final String flinkConfDir = System.getenv("FLINK_CONF_DIR");
        if (StringUtils.isNullOrWhitespaceOnly(flinkConfDir)) {
            return null;
        }
        return Paths.get(flinkConfDir);
    }

    public static Path getFlinkConfDir() {
        final Path flinkConfDirEnv = getFlinkConfDirEnv();
        if (flinkConfDirEnv == null || Files.notExists(flinkConfDirEnv)) {
            final Path flinkHomeEnv = getFlinkHomeEnv();
            if (flinkHomeEnv != null && Files.exists(flinkHomeEnv)) {
                return flinkHomeEnv.resolve("conf");
            }
        }
        return null;
    }

    public static Path getFlinkPluginsDir(Path flinkHome) {
        return flinkHome.resolve("plugins");
    }

    public static Path getFlinkLibDir(Path flinkHome) {
        return flinkHome.resolve("lib");
    }

    public static Path getFlinkExamplesDir(Path flinkHome) {
        return flinkHome.resolve("examples");
    }

    public static Path getFlinkDistJar(Path flinkHome) {
        return getFlinkLibDir(flinkHome).resolve("flink-dist_2.11-1.13.6.jar");
    }

    public static Configuration loadConfiguration() {
        final Path flinkConfDir = getFlinkConfDir();
        if (flinkConfDir != null && Files.exists(flinkConfDir)) {
            return GlobalConfiguration.loadConfiguration(flinkConfDir.toAbsolutePath().toString());
        }
        return GlobalConfiguration.loadConfiguration();

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

    /**
     * @see AbstractContainerizedClusterClientFactory#getClusterSpecification(Configuration)
     */
    public static ClusterSpecification createClusterSpecification(Configuration configuration) {
        int jobManagerMemoryMB = JobManagerProcessUtils.processSpecFromConfigWithNewOptionToInterpretLegacyHeap(configuration, JobManagerOptions.TOTAL_PROCESS_MEMORY).getTotalProcessMemorySize().getMebiBytes();
        int taskManagerMemoryMB = TaskExecutorProcessUtils.processSpecFromConfig(TaskExecutorProcessUtils.getConfigurationMapLegacyTaskManagerHeapSizeToConfigOption(configuration, TaskManagerOptions.TOTAL_PROCESS_MEMORY)).getTotalProcessMemorySize().getMebiBytes();
        int slotsPerTaskManager = configuration.getInteger(TaskManagerOptions.NUM_TASK_SLOTS);
        return (new ClusterSpecification.ClusterSpecificationBuilder()).setMasterMemoryMB(jobManagerMemoryMB).setTaskManagerMemoryMB(taskManagerMemoryMB).setSlotsPerTaskManager(slotsPerTaskManager).createClusterSpecification();
    }

    public static List<JobStatusMessage> listJobs(ClusterClient client) throws Exception {
        return (List<JobStatusMessage>) FutureUtils.checkStateAndGet(client.listJobs());
    }

}
