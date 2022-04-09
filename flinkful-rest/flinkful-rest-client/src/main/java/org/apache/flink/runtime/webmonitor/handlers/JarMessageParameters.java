package org.apache.flink.runtime.webmonitor.handlers;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;
import org.apache.flink.runtime.webmonitor.handlers.JarPlanHandler;
import org.apache.flink.runtime.webmonitor.handlers.JarRunHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Base class of {@link MessageParameters} for {@link JarRunHandler} and {@link JarPlanHandler}.
 */
abstract class JarMessageParameters extends MessageParameters {

    @VisibleForTesting
    public final JarIdPathParameter jarIdPathParameter = new JarIdPathParameter();

    final EntryClassQueryParameter entryClassQueryParameter = new EntryClassQueryParameter();

    final ParallelismQueryParameter parallelismQueryParameter = new ParallelismQueryParameter();

    final ProgramArgsQueryParameter programArgsQueryParameter = new ProgramArgsQueryParameter();

    final ProgramArgQueryParameter programArgQueryParameter = new ProgramArgQueryParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.singletonList(jarIdPathParameter);
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.unmodifiableList(
                Arrays.asList(
                        programArgsQueryParameter,
                        programArgQueryParameter,
                        entryClassQueryParameter,
                        parallelismQueryParameter));
    }
}