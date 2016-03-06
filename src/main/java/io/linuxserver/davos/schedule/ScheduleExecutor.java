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
import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

@Component
public class ScheduleExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleExecutor.class);

    private Map<Long, ScheduledFuture<?>> runningSchedules = new HashMap<>();

    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    private ScheduledExecutorService scheduledExecutorService;

    public ScheduleExecutor() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }
    
    public boolean isScheduleRunning(Long id) {
        return runningSchedules.containsKey(id);
    }

    @PostConstruct
    public void runAutomaticStartupSchedules() {

        LOGGER.info("Initialising automatic startup schedules");

        for (ScheduleConfigurationModel model : scheduleConfigurationDAO.getAll()) {

            if (model.startAutomatically) {

                RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);
                ScheduledFuture<?> runningSchedule = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval,
                        TimeUnit.MINUTES);

                runningSchedules.put(model.id, runningSchedule);
            }
        }

        LOGGER.info("Automatic startup schedules should now be running");
    }

    public void startSchedule(Long id) throws ScheduleAlreadyRunningException {

        if (!runningSchedules.containsKey(id)) {

            ScheduleConfigurationModel model = scheduleConfigurationDAO.getConfig(id);
            RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);

            LOGGER.info("Starting schedule {}", id);
            ScheduledFuture<?> runningSchedule = scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval,
                    TimeUnit.MINUTES);

            runningSchedules.put(model.id, runningSchedule);

        } else {
            throw new ScheduleAlreadyRunningException();
        }
    }

    public void stopSchedule(Long id) throws ScheduleNotRunningException {

        if (runningSchedules.containsKey(id)) {

            LOGGER.info("Stopping schedule {}", id);
            
            if (!runningSchedules.get(id).isCancelled()) {
                
                runningSchedules.get(id).cancel(true);
                runningSchedules.remove(id);
                LOGGER.info("Schedule should now be stopped");
            }
            
        } else {
            throw new ScheduleNotRunningException();
        }
    }
}
