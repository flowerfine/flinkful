package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;

import java.nio.file.Path;

public interface SubmitCommand {

    ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception;
}
