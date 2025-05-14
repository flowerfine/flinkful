package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.sliew.flinkful.kubernetes.controller.core.Controller;

/**
 * 缺少 ack 流程，容易在 worker 挂掉的时候，丢失调度任务
 */
public interface SchedulerInternalQueue {

    void push(Controller controller);

    Controller poll();

    void remvoe(Controller controller);
}
