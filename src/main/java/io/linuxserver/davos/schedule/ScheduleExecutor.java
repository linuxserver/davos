package io.linuxserver.davos.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

@Component
public class ScheduleExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleExecutor.class);
    
    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    private ScheduledExecutorService scheduledExecutorService;

    public ScheduleExecutor() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }

    @PostConstruct
    public void runAutomaticStartupSchedules() {

        LOGGER.info("Initialising automatic startup schedules");
        
        for (ScheduleConfigurationModel model : scheduleConfigurationDAO.getAll()) {

            if (model.startAutomatically) {

                RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);
                scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval, TimeUnit.MINUTES);
            }
        }
        
        LOGGER.info("Automatic startup schedules should now be running");
    }
    
    public void startSchedule(Long id) {
        
        ScheduleConfigurationModel model = scheduleConfigurationDAO.getConfig(id);
        RunnableSchedule runnable = new RunnableSchedule(model.id, scheduleConfigurationDAO);
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, model.interval, TimeUnit.MINUTES);
    }
}
