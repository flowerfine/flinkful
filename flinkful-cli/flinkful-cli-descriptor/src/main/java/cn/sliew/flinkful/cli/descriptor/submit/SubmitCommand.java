package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public interface SubmitCommand {

    JobID submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;
}
