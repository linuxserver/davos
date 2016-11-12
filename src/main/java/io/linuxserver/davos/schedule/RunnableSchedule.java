package io.linuxserver.davos.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class RunnableSchedule implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunnableSchedule.class);
    
    private ScheduleDAO configurationDAO;
    private Long scheduleId;

    public RunnableSchedule(Long scheduleId, ScheduleDAO configurationDAO) {

        this.scheduleId = scheduleId;
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void run() {

        LOGGER.info("Starting schedule {}", scheduleId);
        
        ScheduleModel model = configurationDAO.fetchSchedule(scheduleId);
        
        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);
        ScheduleWorkflow scheduleWorkflow = new ScheduleWorkflow(config);

        scheduleWorkflow.start();
    }
}
