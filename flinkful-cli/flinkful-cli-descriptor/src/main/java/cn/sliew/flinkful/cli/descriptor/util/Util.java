package cn.sliew.flinkful.cli.descriptor.util;

import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.CollectionUtil;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Util {
    ;

    /**
     * 也可以通过 {@link YarnConfigOptions#FLINK_DIST_JAR} 配置 flink-dist-xxx.jar
     * {@link YarnConfigOptions#SHIP_FILES} 配置 ship jars.
     */
    public static void addJarFiles(Configuration config) {
        config.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Arrays.asList(new String[]{"hdfs://hadoop:9000/flink/1.13.6"}));
        config.set(YarnConfigOptions.FLINK_DIST_JAR, "hdfs://hadoop:9000/flink/1.13.6/flink-dist_2.11-1.13.6.jar");
    }

    public static void addJarFiles(YarnClusterDescriptor clusterDescriptor, Configuration config) throws MalformedURLException {
        boolean isRemoteJarPath =
                !CollectionUtil.isNullOrEmpty(config.get(YarnConfigOptions.PROVIDED_LIB_DIRS));
        List<File> shipFiles = new ArrayList<>();
        File[] plugins = new File(FlinkUtil.getFlinkPluginsDir()).listFiles();
        if (plugins != null) {
            for (File plugin : plugins) {
                if (plugin.isDirectory() == false) {
                    continue;
                }
                if (!isRemoteJarPath) {
                    shipFiles.addAll(Arrays.asList(plugin.listFiles()));
                }
            }
        }
        File[] jars = new File(FlinkUtil.getFlinkLibDir()).listFiles();
        if (jars != null) {
            for (File jar : jars) {
                if (jar.toURI().toURL().toString().contains("flink-dist")) {
                    clusterDescriptor.setLocalJarPath(new Path(jar.toURI().toURL().toString()));
                } else if (!isRemoteJarPath) {
                    shipFiles.add(jar);
                }
            }
        }
        clusterDescriptor.addShipFiles(shipFiles);
    }
}
