package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.cli.descriptor.DescriptorSessionCreateCliClient;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.util.Collections;

public enum Util {
    ;

    static CliClient buildCliClient() {
        return new DescriptorCliClient();
    }

    static CliClient buildSessionCreateCliClient() {
        return new DescriptorSessionCreateCliClient();
    }

    static PackageJarJob buildJarJob() {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath(FlinkExamples.EXAMPLE_JAR);
        job.setEntryPointClass(FlinkExamples.EXAMPLE_ENTRY_CLASS);
        job.setProgramArgs(new String[]{});
        job.setClasspaths(Collections.emptyList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
