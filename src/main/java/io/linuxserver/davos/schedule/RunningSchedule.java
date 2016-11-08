package io.linuxserver.davos.schedule;

import java.util.concurrent.ScheduledFuture;

public class RunningSchedule {

    private final ScheduledFuture<?> future;
    private final RunnableSchedule schedule;
    
    public RunningSchedule(ScheduledFuture<?> future, RunnableSchedule schedule) {
        this.future = future;
        this.schedule = schedule;
    }
    
    public ScheduledFuture<?> getFuture() {
        return future;
    }
    
    public RunnableSchedule getSchedule() {
        return schedule;
    }
}
