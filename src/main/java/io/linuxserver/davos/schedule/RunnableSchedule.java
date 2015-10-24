package io.linuxserver.davos.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class RunnableSchedule implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunnableSchedule.class);
    
    private ScheduleConfigurationDAO configurationDAO;
    private Long scheduleId;

    public RunnableSchedule(Long scheduleId, ScheduleConfigurationDAO configurationDAO) {

        this.scheduleId = scheduleId;
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void run() {

        LOGGER.info("Starting schedule {}", scheduleId);
        
        ScheduleConfigurationModel model = configurationDAO.getConfig(scheduleId);
        
        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);
        ScheduleWorkflow scheduleWorkflow = new ScheduleWorkflow(config);

        scheduleWorkflow.start();
        
        configurationDAO.updateLastRun(scheduleId, config.getLastRun());
    }
}
