package cn.sliew.flinkful.cli.descriptor.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.util.Collections;

public enum Util {
    ;

    public static CliClient buildCliClient() {
        return new DescriptorCliClient();
    }

    public static PackageJarJob buildJarJob() {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath(FlinkExamples.EXAMPLE_JAR_URL);
        job.setEntryPointClass(FlinkExamples.EXAMPLE_ENTRY_CLASS);
        job.setProgramArgs(new String[]{});
        job.setClasspaths(Collections.emptyList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
