package io.linuxserver.davos.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.exception.ScheduleAlreadyRunningException;
import io.linuxserver.davos.exception.ScheduleNotRunningException;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.ScheduleModel;

@Component
public class ScheduleExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleExecutor.class);

    private Map<Long, RunningSchedule> runningSchedules = new HashMap<>();

    @Resource
    private ScheduleDAO scheduleConfigurationDAO;

    private ScheduledExecutorService scheduledExecutorService;

    public ScheduleExecutor() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }

    public boolean isScheduleRunning(Long id) {
        return runningSchedules.containsKey(id);
    }
    
    public RunningSchedule getRunningSchedule(Long id) {
        return runningSchedules.get(id);
    }

    @PostConstruct
    public void runAutomaticStartupSchedules() {

        LOGGER.info("Initialising automatic startup schedules");

        for (ScheduleModel model : scheduleConfigurationDAO.getAll()) {

            if (model.getStartAutomatically()) {

                RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);
                ScheduledFuture<?> runningSchedule = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval,
                        TimeUnit.MINUTES);

                runningSchedules.put(model.id, new RunningSchedule(runningSchedule, runnable));
            }
        }

        LOGGER.info("Automatic startup schedules should now be running");
    }

    public void startSchedule(Long id) throws ScheduleAlreadyRunningException {

        if (!runningSchedules.containsKey(id)) {

            ScheduleModel model = scheduleConfigurationDAO.fetchSchedule(id);
            RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);

            LOGGER.info("Starting schedule {}", id);
            ScheduledFuture<?> runningSchedule = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval,
                    TimeUnit.MINUTES);

            runningSchedules.put(model.id, new RunningSchedule(runningSchedule, runnable));

        } else {
            throw new ScheduleAlreadyRunningException();
        }
    }

    public void stopSchedule(Long id) throws ScheduleNotRunningException {

        if (runningSchedules.containsKey(id)) {

            LOGGER.info("Stopping schedule {}", id);

            ScheduledFuture<?> future = runningSchedules.get(id).getFuture();
            
            if (!future.isCancelled()) {

                future.cancel(true);
                runningSchedules.remove(id);
                LOGGER.info("Schedule should now be stopped");
            }

        } else {
            throw new ScheduleNotRunningException();
        }
    }
}
