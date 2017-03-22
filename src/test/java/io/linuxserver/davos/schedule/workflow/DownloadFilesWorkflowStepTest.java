package io.linuxserver.davos.schedule.workflow;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.schedule.workflow.actions.MoveFileAction;
import io.linuxserver.davos.schedule.workflow.actions.PostDownloadAction;
import io.linuxserver.davos.schedule.workflow.transfer.FTPTransfer;
import io.linuxserver.davos.schedule.workflow.transfer.TransferStrategy;
import io.linuxserver.davos.schedule.workflow.transfer.TransferStrategyFactory;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.exception.DownloadFailedException;
import io.linuxserver.davos.util.FileUtils;

public class DownloadFilesWorkflowStepTest {

    @InjectMocks
    private DownloadFilesWorkflowStep workflowStep = new DownloadFilesWorkflowStep();

    @Mock(name = "nextStep")
    private WorkflowStep mockNextStep;

    @Mock(name = "backoutStep")
    private WorkflowStep mockBackoutStep;
    
    @Mock
    private Connection mockConnection;

    @Mock
    private TransferStrategyFactory mockTransferStrategyFactory;

    @Mock
    private FileUtils mockFileUtils;
    
    @Captor
    private ArgumentCaptor<FTPTransfer> transferCaptor;
    
    private TransferStrategy mockTransferStrategy;

    @Before
    public void setUp() {

        initMocks(this);

        mockTransferStrategy = mock(TransferStrategy.class);
        
        when(mockTransferStrategyFactory.getStrategy(any(FileTransferType.class), eq(mockConnection)))
                .thenReturn(mockTransferStrategy);
    }

    @Test
    public void shouldCallStrategyFactoryToGetCorrectStrategyAndPassFileThrough() {

        ArrayList<FTPFile> filesToDownload = new ArrayList<FTPFile>();

        FTPFile file = new FTPFile("", 0, "", 0, false);
        FTPFile file2 = new FTPFile("", 0, "", 0, false);

        filesToDownload.add(file);
        filesToDownload.add(file2);

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "local/", FileTransferType.FILE, false, false, false);
        
        ArrayList<PostDownloadAction> actions = new ArrayList<PostDownloadAction>();
        actions.add(new MoveFileAction("", ""));
        
        config.setActions(actions);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);
        schedule.getFilesToDownload().addAll(filesToDownload.stream().map(f -> new FTPTransfer(f)).collect(toList()));
        
        workflowStep.runStep(schedule);

        verify(mockTransferStrategyFactory).getStrategy(FileTransferType.FILE, mockConnection);
        
        verify(mockTransferStrategy).setPostDownloadActions(actions);
        verify(mockTransferStrategy, times(2)).transferFile(transferCaptor.capture(), eq("local/"));
        
        assertThat(transferCaptor.getAllValues().get(0).getFile()).isEqualTo(file);
        assertThat(transferCaptor.getAllValues().get(1).getFile()).isEqualTo(file2);
    }
    
    @Test
    public void shouldCallOnNextStepWhenFinished() {
        
        ArrayList<FTPFile> filesToDownload = new ArrayList<FTPFile>();
        FTPFile file = new FTPFile("", 0, "", 0, false);
        filesToDownload.add(file);

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "local/", FileTransferType.FILE, false, false, false);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.getFilesToDownload().addAll(filesToDownload.stream().map(f -> new FTPTransfer(f)).collect(toList()));
        schedule.setConnection(mockConnection);
        workflowStep.runStep(schedule);
        
        InOrder inOrder = Mockito.inOrder(mockTransferStrategy, mockNextStep);
        
        inOrder.verify(mockTransferStrategy).transferFile(any(FTPTransfer.class), eq("local/"));
        inOrder.verify(mockNextStep).runStep(schedule);
    }
    
    @Test
    public void ifStrategyTranferFailsThenShouldStillCallNextStep() {
        
        ArrayList<FTPFile> filesToDownload = new ArrayList<FTPFile>();
        FTPFile file = new FTPFile("", 0, "", 0, false);
        filesToDownload.add(file);

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "local/", FileTransferType.FILE, false, false, false);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.getFilesToDownload().addAll(filesToDownload.stream().map(f -> new FTPTransfer(f)).collect(toList()));
        schedule.setConnection(mockConnection);

        doThrow(new DownloadFailedException()).when(mockTransferStrategy).transferFile(any(FTPTransfer.class), eq("local/"));
        
        workflowStep.runStep(schedule);
        
        verify(mockNextStep).runStep(schedule);
    }
    
    @Test
    public void shouldDeleteHostFileIfOptionSet() {
        
        ArrayList<FTPFile> filesToDownload = new ArrayList<FTPFile>();
        FTPFile file = new FTPFile("", 0, "", 0, false);
        filesToDownload.add(file);

        ScheduleConfiguration config = new ScheduleConfiguration("", TransferProtocol.SFTP, "", 0, new UserCredentials("", ""),
                "", "local/", FileTransferType.FILE, false, false, true);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.getFilesToDownload().addAll(filesToDownload.stream().map(f -> new FTPTransfer(f)).collect(toList()));
        schedule.setConnection(mockConnection);
        workflowStep.runStep(schedule);
        
        verify(mockConnection).deleteRemoteFile(file);
    }
}
