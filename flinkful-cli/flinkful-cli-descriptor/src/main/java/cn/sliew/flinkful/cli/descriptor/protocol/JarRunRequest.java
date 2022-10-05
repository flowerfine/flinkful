package cn.sliew.flinkful.cli.descriptor.protocol;

import cn.sliew.flinkful.shade.org.apache.flink.api.common.JobID;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JarRunRequest {

    private boolean allowNonRestoredState;

    private String savepointPath;

    private String programArgs;

    private List<String> programArgsList;

    private String entryClass;

    private Integer parallelism;

    private JobID jobId;
}
