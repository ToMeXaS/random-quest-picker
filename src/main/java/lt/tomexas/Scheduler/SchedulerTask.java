package lt.tomexas.Scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulerTask {
    private ScheduledFuture<?> selectorHandler = null;
    private final ScheduledExecutorService selectorScheduler =
            Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> UpdateUIHandler = null;
    private final ScheduledExecutorService UpdateUIScheduler =
            Executors.newScheduledThreadPool(1);

    public void runQuestSelector(Runnable callback) {
        selectorHandler = selectorScheduler.scheduleAtFixedRate(callback, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void runUpdateUI(Runnable callback) {
        UpdateUIHandler = UpdateUIScheduler.scheduleAtFixedRate(callback, 0, 1, TimeUnit.SECONDS);
    }

    public void cancelIfNeeded(){
        if (selectorHandler != null) selectorHandler.cancel(true);
    }

    public void cancelSelector() { selectorHandler.cancel(true); }
}