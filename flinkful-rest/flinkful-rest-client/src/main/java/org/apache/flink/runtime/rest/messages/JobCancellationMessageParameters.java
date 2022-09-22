package org.apache.flink.runtime.rest.messages;

import org.apache.flink.api.common.JobID;

import java.util.Collection;
import java.util.Collections;

/**
 * Parameters for job related REST handlers.
 *
 * <p>A job related REST handler always requires a {@link JobIDPathParameter}.
 */
public class JobCancellationMessageParameters extends MessageParameters {

    public final JobIDPathParameter jobPathParameter = new JobIDPathParameter();
    public final TerminationModeQueryParameter terminationModeQueryParameter =
            new TerminationModeQueryParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.singleton(jobPathParameter);
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.singleton(terminationModeQueryParameter);
    }

    public JobCancellationMessageParameters resolveJobId(JobID jobId) {
        jobPathParameter.resolve(jobId);
        return this;
    }

    public JobCancellationMessageParameters resolveTerminationMode(
            TerminationModeQueryParameter.TerminationMode terminationMode) {
        terminationModeQueryParameter.resolve(Collections.singletonList(terminationMode));
        return this;
    }
}