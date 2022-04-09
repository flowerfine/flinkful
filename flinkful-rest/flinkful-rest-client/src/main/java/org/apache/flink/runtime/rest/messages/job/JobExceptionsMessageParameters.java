package org.apache.flink.runtime.rest.messages.job;

import org.apache.flink.runtime.rest.handler.job.JobExceptionsHandler;
import org.apache.flink.runtime.rest.messages.JobMessageParameters;
import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link MessageParameters} for {@link JobExceptionsHandler}.
 */
public class JobExceptionsMessageParameters extends JobMessageParameters {

    public final UpperLimitExceptionParameter upperLimitExceptionParameter =
            new UpperLimitExceptionParameter();

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.singletonList(upperLimitExceptionParameter);
    }
}