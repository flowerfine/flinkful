package org.apache.flink.runtime.rest.messages.job.metrics;

import org.apache.flink.runtime.rest.handler.job.metrics.JobVertexMetricsHandler;
import org.apache.flink.runtime.rest.messages.JobVertexMessageParameters;
import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link MessageParameters} for {@link JobVertexMetricsHandler}.
 */
public class JobVertexMetricsMessageParameters extends JobVertexMessageParameters {

    public final MetricsFilterParameter metricsFilterParameter = new MetricsFilterParameter();

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.singletonList(metricsFilterParameter);
    }
}
