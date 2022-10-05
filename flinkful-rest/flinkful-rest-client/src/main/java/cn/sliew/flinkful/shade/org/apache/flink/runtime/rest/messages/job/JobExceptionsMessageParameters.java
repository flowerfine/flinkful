package cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.job;

import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.handler.job.JobExceptionsHandler;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.JobMessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.MessageParameters;
import cn.sliew.flinkful.shade.org.apache.flink.runtime.rest.messages.MessageQueryParameter;

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