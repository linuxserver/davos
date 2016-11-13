package io.linuxserver.davos.schedule.workflow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.schedule.workflow.transfer.FTPTransfer;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class ScheduleWorkflow {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleWorkflow.class);
    
    private ScheduleConfiguration config;
    
    private Client client;
    private Connection connection;
    private List<String> filesFromLastScan = new ArrayList<>();
    private List<FTPTransfer> filesToDownload = new ArrayList<>();
    
    public ScheduleWorkflow(ScheduleConfiguration config) {
        this.config = config;
    }
    
    protected Client getClient() {
        return client;
    }

    protected ScheduleConfiguration getConfig() {
        return config;
    }

    protected Connection getConnection() {
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

    public List<String> getFilesFromLastScan() {
        return filesFromLastScan;
    }

    public List<FTPTransfer> getFilesToDownload() {
        return filesToDownload;
    }
}
