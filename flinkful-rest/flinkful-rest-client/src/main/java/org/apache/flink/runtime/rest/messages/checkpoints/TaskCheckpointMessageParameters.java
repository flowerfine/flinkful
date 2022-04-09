package org.apache.flink.runtime.rest.messages.checkpoints;

import org.apache.flink.runtime.rest.messages.JobVertexIdPathParameter;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;

import java.util.Arrays;
import java.util.Collection;

/**
 * Message parameters for subtask related checkpoint message.
 *
 * <p>The message requires a JobID, checkpoint ID and a JobVertexID to be specified.
 */
public class TaskCheckpointMessageParameters extends CheckpointMessageParameters {

    public final JobVertexIdPathParameter jobVertexIdPathParameter =
            new JobVertexIdPathParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Arrays.asList(jobPathParameter, checkpointIdPathParameter, jobVertexIdPathParameter);
    }
}