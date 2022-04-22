package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

public interface Command {

    JobID submit(Configuration configuration, PackageJarJob job) throws Exception;
}
