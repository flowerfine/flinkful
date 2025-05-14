package cn.sliew.flinkful.kubernetes.controller.core.inmemory;

import cn.hutool.core.thread.ThreadUtil;
import cn.sliew.carp.framework.spring.lifecycel.AbstractLifecycle;
import cn.sliew.flinkful.kubernetes.controller.core.Controller;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerWorker extends AbstractLifecycle {

    private final SchedulerInternalQueue internalQueue;
    private final ControllerInvoker invoker;

    private ScheduledThreadPoolExecutor scheduledExecutor;
    private ScheduledFuture<?> scheduledFuture;

    private Timer wheelTimer;
    private final Map<String, Timeout> controllerMap = new ConcurrentHashMap<>(8);

    @Override
    protected void doStart() throws Exception {
        this.wheelTimer = new HashedWheelTimer(new DefaultThreadFactory("scheduler-worker", true), 1, TimeUnit.SECONDS, 512);
        scheduledExecutor = ThreadUtil.createScheduledExecutor(1);
        scheduledFuture = scheduledExecutor.scheduleWithFixedDelay(this::run, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void doStop() throws Exception {
        scheduledFuture.cancel(true);
        scheduledExecutor.shutdown();

        controllerMap.forEach((key, value) -> value.cancel());
        controllerMap.clear();
        wheelTimer.stop();
    }

    private void run() {
        try {
            Thread.currentThread().setName("scheduler-server");
            Controller controller = internalQueue.poll();
            if (controller != null) {
                dispatch(controller);
            }
        } catch (Exception e) {
            log.error("Scheduler Worker error", e);
        }
    }

    public void dispatch(Controller controller) {
        if (!isRunning()) {
            log.warn("Scheduler Worker is not started, can't add controller.");
            return;
        }
        WheelTimerTask timerJob = new WheelTimerTask(controller, invoker);
        Duration interval = controller.getInterval();
        Timeout timeout = wheelTimer.newTimeout(timerJob, interval.toSeconds(), TimeUnit.SECONDS);
        controllerMap.put(controller.getId(), timeout);
    }
}
