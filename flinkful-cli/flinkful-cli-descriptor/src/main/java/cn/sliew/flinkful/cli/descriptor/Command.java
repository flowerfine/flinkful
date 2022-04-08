package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.configuration.Configuration;

public interface Command {

    void submit(Configuration configuration, PackageJarJob job) throws Exception;
}
