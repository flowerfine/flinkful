package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.sliew.flinkful.kubernetes.controller.core.Controller;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemorySchedulerInternalQueue implements SchedulerInternalQueue {

    private final ConcurrentLinkedQueue<Controller> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void push(Controller controller) {
        queue.add(controller);
    }

    @Override
    public Controller poll() {
        return queue.poll();
    }

    @Override
    public void remvoe(Controller controller) {
        queue.remove(controller);
    }
}
