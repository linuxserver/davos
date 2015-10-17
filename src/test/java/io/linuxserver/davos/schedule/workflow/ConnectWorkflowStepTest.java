package io.linuxserver.davos.schedule.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.ClientFactory;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.exception.ClientConnectionException;

public class ConnectWorkflowStepTest {

    @InjectMocks
    private ConnectWorkflowStep workflowStep = new ConnectWorkflowStep();

    @Mock
    private ClientFactory mockClientFactory;

    @Mock
    private Client mockClient;
    
    @Mock
    private WorkflowStep mockNextStep;

    @Before
    public void setUp() {

        initMocks(this);

        when(mockClientFactory.getClient(TransferProtocol.SFTP)).thenReturn(mockClient);
    }

    @Test
    public void runStepShouldCreateNewClient() {

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");

        workflowStep.runStep(new ScheduleWorkflow(config));

        verify(mockClientFactory).getClient(TransferProtocol.SFTP);
    }

    @Test
    public void runStepShouldSetClientIntoWorkflow() {

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        workflowStep.runStep(schedule);

        assertThat(schedule.getClient()).isEqualTo(mockClient);
    }

    @Test
    public void runStepShouldConnectToNewlyCreatedClient() {

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");

        workflowStep.runStep(new ScheduleWorkflow(config));

        verify(mockClient).connect();
    }

    @Test
    public void runStepShouldConnectToTheClientUsingTheConfigsHostAndCredentialInformation() {

        String hostIP = "123.456.789.0";
        int port = 1337;
        UserCredentials credentials = new UserCredentials(hostIP, hostIP);

        ScheduleConfiguration config = new ScheduleConfiguration("scheduleName", TransferProtocol.SFTP, hostIP, port, credentials,
                "remotePath", "localPath");

        workflowStep.runStep(new ScheduleWorkflow(config));

        InOrder inOrder = Mockito.inOrder(mockClient);

        inOrder.verify(mockClient).setCredentials(credentials);
        inOrder.verify(mockClient).setHost(hostIP);
        inOrder.verify(mockClient).setPort(port);
        inOrder.verify(mockClient).connect();
    }

    @Test
    public void runStepShouldPlaceConnectedClientConnectionIntoSchedule() {

        Connection mockConnection = mock(Connection.class);
        when(mockClient.connect()).thenReturn(mockConnection);
        
        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        workflowStep.runStep(schedule);

        assertThat(schedule.getConnection()).isEqualTo(mockConnection);
    }
    
    @Test
    public void runStepShouldCallOnNextStepWhenComplete() {
        
        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");
        
        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        workflowStep.runStep(schedule);

        InOrder inOrder = Mockito.inOrder(mockClient, mockNextStep);

        inOrder.verify(mockClient).connect();
        inOrder.verify(mockNextStep).runStep(schedule);
    }
    
    @Test
    public void ifClientCannotConnectThenDoNotCallNextStep() {
        
        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "");
        
        when(mockClient.connect()).thenThrow(new ClientConnectionException());
        
        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        workflowStep.runStep(schedule);

        InOrder inOrder = Mockito.inOrder(mockClient, mockNextStep);

        inOrder.verify(mockClient).connect();
        inOrder.verify(mockNextStep, never()).runStep(schedule);
        
    }
}
