package cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.JobVertexMessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.MessagePathParameter;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.SubtaskIndexPathParameter;

import java.util.Arrays;
import java.util.Collection;

/**
 * Message parameters for subtask REST handlers.
 */
public class SubtaskMessageParameters extends JobVertexMessageParameters {

    public final SubtaskIndexPathParameter subtaskIndexPathParameter =
            new SubtaskIndexPathParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Arrays.asList(jobPathParameter, jobVertexIdPathParameter, subtaskIndexPathParameter);
    }
}