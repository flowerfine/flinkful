package cn.sliew.flinkful.cli.frontend.example;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.frontend.FrontendCliClient;
import cn.sliew.flinkful.common.examples.FlinkExamples;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.util.Collections;

public enum Util {
    ;

    static CliClient buildCliClient() {
        return new FrontendCliClient();
    }

    static PackageJarJob buildJarJob() {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath(FlinkExamples.EXAMPLE_JAR_URL);
        job.setEntryPointClass(FlinkExamples.EXAMPLE_ENTRY_CLASS);
        job.setProgramArgs(new String[]{});
        job.setClasspaths(Collections.emptyList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
