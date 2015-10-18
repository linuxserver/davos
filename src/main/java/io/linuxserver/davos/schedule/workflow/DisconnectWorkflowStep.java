package io.linuxserver.davos.schedule.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public class DisconnectWorkflowStep extends WorkflowStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectWorkflowStep.class);
    
    @Override
    public void runStep(ScheduleWorkflow schedule) {

        try {
            schedule.getClient().disconnect();
        } catch (FTPException e) {
            LOGGER.error("Unable to disconnect from host. Error was: {}", e.getMessage());
            LOGGER.debug("Stacktrace", e);
        }
    }
}
