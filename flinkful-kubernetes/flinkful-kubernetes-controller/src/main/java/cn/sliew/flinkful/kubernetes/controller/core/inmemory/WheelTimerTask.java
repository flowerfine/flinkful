package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.sliew.flinkful.kubernetes.controller.core.Controller;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WheelTimerTask implements TimerTask {

    private final Controller controller;
    private final ControllerInvoker invoker;

    public WheelTimerTask(Controller controller, ControllerInvoker invoker) {
        this.controller = controller;
        this.invoker = invoker;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        invoker.invoke(timeout, controller);
    }
}
