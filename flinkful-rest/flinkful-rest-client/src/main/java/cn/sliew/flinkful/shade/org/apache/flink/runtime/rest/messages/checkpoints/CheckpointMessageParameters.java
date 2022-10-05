package cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.checkpoints;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.JobMessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.MessagePathParameter;

import java.util.Arrays;
import java.util.Collection;

/**
 * Message parameters for checkpoint related messages.
 */
public class CheckpointMessageParameters extends JobMessageParameters {

    public final CheckpointIdPathParameter checkpointIdPathParameter = new CheckpointIdPathParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Arrays.asList(jobPathParameter, checkpointIdPathParameter);
    }
}