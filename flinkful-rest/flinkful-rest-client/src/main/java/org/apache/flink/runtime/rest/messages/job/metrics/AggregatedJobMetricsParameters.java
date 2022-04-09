package org.apache.flink.runtime.rest.messages.job.metrics;

import org.apache.flink.runtime.rest.messages.MessagePathParameter;

import java.util.Collection;
import java.util.Collections;

/**
 * Parameters for aggregating job metrics.
 */
public class AggregatedJobMetricsParameters
        extends AbstractAggregatedMetricsParameters<JobsFilterQueryParameter> {

    public AggregatedJobMetricsParameters() {
        super(new JobsFilterQueryParameter());
    }

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.emptyList();
    }
}