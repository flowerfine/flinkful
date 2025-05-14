package cn.sliew.flinkful.kubernetes.controller.core;

import cn.sliew.flinkful.kubernetes.controller.core.inmemory.SchedulerServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultScheduler implements Scheduler {

    private final SchedulerServer server;

    @Override
    public void schedule(Controller controller) {
        server.add(controller);
    }

    @Override
    public void unschedule(Controller controller) {
        server.remove(controller);
    }
}
