package org.apache.flink.runtime.rest.messages.job.metrics;

import org.apache.flink.runtime.rest.handler.job.metrics.SubtaskMetricsHandler;
import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;
import org.apache.flink.runtime.rest.messages.job.SubtaskMessageParameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link MessageParameters} for {@link SubtaskMetricsHandler}.
 */
public class SubtaskMetricsMessageParameters extends SubtaskMessageParameters {

    public final MetricsFilterParameter metricsFilterParameter = new MetricsFilterParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.unmodifiableCollection(
                Arrays.asList(
                        jobPathParameter, jobVertexIdPathParameter, subtaskIndexPathParameter));
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.singletonList(metricsFilterParameter);
    }
}