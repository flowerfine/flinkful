package cn.sliew.flinkful.cli.descriptor.protocol;

import lombok.Getter;
import lombok.Setter;
import org.apache.flink.api.common.JobID;

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
