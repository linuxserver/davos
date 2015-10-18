package io.linuxserver.davos.schedule.workflow;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.exception.ClientDisconnectException;

public class DisconnectWorkflowStepTest {

    private DisconnectWorkflowStep workflowStep = new DisconnectWorkflowStep();

    @Mock
    private Client mockClient;
    
    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @Test
    public void runStepShouldCloseTheConnection() {
        
        ScheduleConfiguration config = new ScheduleConfiguration(0L, "", TransferProtocol.SFTP, "", 0,
                new UserCredentials("", ""), "", "", FileTransferType.FILES_ONLY);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);
        schedule.setClient(mockClient);
        
        workflowStep.runStep(schedule);
        
        verify(mockClient).disconnect();
    }
    
    @Test
    public void ifDisconnectingFailsThenDoNothing() {
        
        ScheduleConfiguration config = new ScheduleConfiguration(0L, "", TransferProtocol.SFTP, "", 0,
                new UserCredentials("", ""), "", "", FileTransferType.FILES_ONLY);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);
        schedule.setClient(mockClient);
        
        doThrow(new ClientDisconnectException()).when(mockClient).disconnect();
        
        workflowStep.runStep(schedule);
    }
}
