package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;

public interface SubmitCommand {

    JobID submit(Configuration configuration, PackageJarJob job) throws Exception;
}
