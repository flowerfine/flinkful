package org.apache.flink.runtime.rest.messages.job.metrics;

import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Base {@link MessageParameters} class for aggregating metrics.
 */
public abstract class AbstractAggregatedMetricsParameters<M extends MessageQueryParameter<?>>
        extends MessageParameters {
    public final MetricsFilterParameter metrics = new MetricsFilterParameter();
    public final MetricsAggregationParameter aggs = new MetricsAggregationParameter();
    public final M selector;

    AbstractAggregatedMetricsParameters(M selector) {
        this.selector = selector;
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(metrics, aggs, selector));
    }
}