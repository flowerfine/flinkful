package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.sliew.flinkful.kubernetes.controller.core.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerServer {

    private final SchedulerInternalQueue internalQueue;

    public void add(Controller controller) {
        internalQueue.push(controller);
    }

    public void remove(Controller controller) {
        internalQueue.remvoe(controller);
    }
}
