package cn.sliew.flinkful.examples.common;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;

public enum FlinkExamples {
    ;

    private static final String FLINK_HOME = System.getenv("FLINK_HOME");

    private static final String EXAMPLES_DIR = FLINK_HOME + "/examples";
    private static final String LIB_DIR = FLINK_HOME + "/lib";

    public static final String EXAMPLE_JAR = EXAMPLES_DIR + "/streaming/TopSpeedWindowing.jar";
    public static final String EXAMPLE_ENTRY_CLASS = "org.apache.flink.streaming.examples.windowing.TopSpeedWindowing";

    public static Configuration loadConfiguration() {
        return GlobalConfiguration.loadConfiguration();
    }

}
