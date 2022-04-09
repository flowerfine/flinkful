package org.apache.flink.runtime.rest.messages.job;

import org.apache.flink.runtime.rest.messages.MessagePathParameter;

import java.util.Arrays;
import java.util.Collection;

/** The type Subtask attempt message parameters. */
public class SubtaskAttemptMessageParameters extends SubtaskMessageParameters {

    public final SubtaskAttemptPathParameter subtaskAttemptPathParameter =
            new SubtaskAttemptPathParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Arrays.asList(
                jobPathParameter,
                jobVertexIdPathParameter,
                subtaskIndexPathParameter,
                subtaskAttemptPathParameter);
    }
}