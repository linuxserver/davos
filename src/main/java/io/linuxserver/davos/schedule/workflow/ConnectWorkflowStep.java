package io.linuxserver.davos.schedule.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.ClientFactory;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public class ConnectWorkflowStep extends WorkflowStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectWorkflowStep.class);

    private ClientFactory clientFactory = new ClientFactory();

    public ConnectWorkflowStep() {
        this.nextStep = new FilterFilesWorkflowStep();
    }

    @Override
    public void runStep(ScheduleWorkflow schedule) {

        Client client = clientFactory.getClient(schedule.getConfig().getConnectionType());

        client.setCredentials(schedule.getConfig().getCredentials());
        client.setHost(schedule.getConfig().getHostName());
        client.setPort(schedule.getConfig().getPort());

        try {

            schedule.setConnection(client.connect());
            schedule.setClient(client);

            nextStep.runStep(schedule);

        } catch (FTPException e) {

            LOGGER.warn("Unable to create connection to {} on port {}. Falling back. Will try again next time.",
                    schedule.getConfig().getHostName(), schedule.getConfig().getPort());
            LOGGER.warn("Error was: {}", e.getMessage());
            LOGGER.debug("Stacktrace", e);
        }
    }
}
