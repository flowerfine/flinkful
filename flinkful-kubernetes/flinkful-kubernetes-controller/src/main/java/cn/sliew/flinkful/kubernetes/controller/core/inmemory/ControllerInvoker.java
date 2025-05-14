package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.sliew.flinkful.kubernetes.controller.core.Controller;
import io.netty.util.Timeout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ControllerInvoker {

    private final SchedulerInternalQueue internalQueue;

    public void invoke(Timeout timeout, Controller controller) {
        controller.run();
        // 不需要执行的时候，不在推送至 queue 中，终止执行
        if (Objects.nonNull(controller.getInterval())) {
            internalQueue.push(controller);
        }
    }
}
