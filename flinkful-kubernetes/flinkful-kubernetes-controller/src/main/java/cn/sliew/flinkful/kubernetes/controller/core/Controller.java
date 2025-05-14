package cn.sliew.flinkful.kubernetes.controller.core;

import cn.sliew.milky.common.concurrent.RunnableWrapper;

import java.time.Duration;

public interface Controller extends RunnableWrapper {

    String getId();

    Duration getInterval();
}
