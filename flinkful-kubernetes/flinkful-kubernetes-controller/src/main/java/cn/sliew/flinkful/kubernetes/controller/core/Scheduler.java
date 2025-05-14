package cn.sliew.flinkful.kubernetes.controller.core;

public interface Scheduler {

    void schedule(Controller controller);

    void unschedule(Controller controller);
}
