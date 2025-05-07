package cn.sliew.flinkful.kubernetes.operator.util;

import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;

import java.util.Arrays;
import java.util.List;

public enum Ports {
    ;

    public static final ContainerPort FLINK_JMX_PORT = new ContainerPortBuilder()
            .withName("jmx-metrics")
            .withContainerPort(8789)
            .withProtocol("TCP")
            .build();

    public static final ContainerPort FLINK_PROM_PORT = new ContainerPortBuilder()
            .withName("prom-metrics")
            .withContainerPort(9249)
            .withProtocol("TCP")
            .build();

    public static final List<ContainerPort> FLINK_METRICS_PORTS = Arrays.asList(FLINK_JMX_PORT, FLINK_PROM_PORT);
}
