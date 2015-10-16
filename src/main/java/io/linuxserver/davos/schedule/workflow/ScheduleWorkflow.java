package io.linuxserver.davos.schedule.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.schedule.workflow.steps.ConnectWorkflowStep;

public class ScheduleWorkflow implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleWorkflow.class);
    
    private ScheduleConfiguration config;
    
    public ScheduleWorkflow(ScheduleConfiguration config) {
        this.config = config;
    }
    
    @Override
    public void run() {
        
        LOGGER.info("Running schedule: {}", config.getScheduleName());
        new ConnectWorkflowStep().runSchedule(this);
        LOGGER.info("Finished schedule run: {}", config.getScheduleName());
    }

    public ScheduleConfiguration getConfig() {
        return config;
    }
}
