package cn.sliew.flinkful.cli.base;

import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

public enum FlinkUtil {
    ;

    public static String getHadoopHome() {
//        return "/Users/wangqi/Documents/software/hadoop/hadoop-3.2.1";
        return System.getenv("HADOOP_HOME");
    }

    public static String getFlinkHome() {
//        return "/Users/wangqi/Documents/software/flink/flink-1.13.6";
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


}
