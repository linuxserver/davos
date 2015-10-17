package io.linuxserver.davos.schedule.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class ScheduleWorkflow {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleWorkflow.class);
    
    private ScheduleConfiguration config;
    private Client client;
    private Connection connection;
    
    public ScheduleWorkflow(ScheduleConfiguration config) {
        
        this.config = config;
    }
    
    public Client getClient() {
        return client;
    }

    public ScheduleConfiguration getConfig() {
        return config;
    }

    public Connection getConnection() {
        return connection;
    }

    public void start() {
        
        LOGGER.info("Running schedule: {}", config.getScheduleName());
        new ConnectWorkflowStep().runStep(this);
        LOGGER.info("Finished schedule run: {}", config.getScheduleName());
    }

    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected void setClient(Client client) {
        this.client = client;
    }
}
