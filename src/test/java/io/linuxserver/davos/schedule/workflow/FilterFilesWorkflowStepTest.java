package io.linuxserver.davos.schedule.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.exception.FileListingException;

public class FilterFilesWorkflowStepTest {

    @InjectMocks
    private FilterFilesWorkflowStep workflowStep = new FilterFilesWorkflowStep();

    @Mock(name = "nextStep")
    private DownloadFilesWorkflowStep mockNextStep;

    @Mock(name = "backoutStep")
    private DisconnectWorkflowStep mockBackupStep;

    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void workflowStepShouldListFilesInTheRemoteDirectory() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockConnection).listFiles("remote/");
    }

    @Test
    public void workflowStepShouldFilterOutAnyFilesThatAreNotInTheGivenConfigList() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setFilters(Arrays.asList("file1", "file2", "file4"));
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file2 = new FTPFile("file2", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file3 = new FTPFile("file3", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file4 = new FTPFile("file4", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file5 = new FTPFile("file5", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockNextStep).setFilesToDownload(Arrays.asList(file1, file2, file4));
    }

    @Test
    public void workflowStepShouldFilterOutAnyFilesThatAreNotInTheGivenConfigListAndWereModifiedBeforeLastRun() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setFilters(Arrays.asList("file1", "file2", "file4"));
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file2 = new FTPFile("file2", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file3 = new FTPFile("file3", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file4 = new FTPFile("file4", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file5 = new FTPFile("file5", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockNextStep).setFilesToDownload(Arrays.asList(file2));
    }
    
    @Test
    public void shouldOnlyAddOneInstanceOfAFileEvenIfTwoFiltersMatch() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setFilters(Arrays.asList("file1", "file2", "file2"));
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file2 = new FTPFile("file2", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file3 = new FTPFile("file3", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file4 = new FTPFile("file4", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file5 = new FTPFile("file5", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockNextStep).setFilesToDownload(Arrays.asList(file2));
    }

    @Test
    public void workflowStepShouldFilterOutAnyFilesThatDoNotMatchTheWildcards() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setFilters(Arrays.asList("file1?and?Stuff", "file2*something", "file4*", "file5"));
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1.and-stuff", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file2 = new FTPFile("file2.andMoreTextsomething", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file3 = new FTPFile("file3", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file4 = new FTPFile("file4.txt", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file5 = new FTPFile("file5.txt", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockNextStep).setFilesToDownload(Arrays.asList(file2, file4));
    }

    @Test
    public void workflowStepShouldCallNextStepRunMethodOnceSettingFilters() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setFilters(Arrays.asList("file1", "file2", "file4"));
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        InOrder inOrder = Mockito.inOrder(mockNextStep);

        assertThat(config.getLastRun().toString("yyyy-MM-dd hh:mm")).isEqualTo(DateTime.now().toString("yyyy-MM-dd hh:mm"));

        inOrder.verify(mockNextStep).setFilesToDownload(Arrays.asList(file1));
        inOrder.verify(mockNextStep).runStep(schedule);
    }

    @Test
    public void ifFilterListIsInitiallyEmptyThenAssumeThatAllFilesAfterLastRunShouldBeDownloaded() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);
        config.setLastRun(DateTime.now().minusDays(1));

        ArrayList<FTPFile> files = new ArrayList<FTPFile>();

        FTPFile file1 = new FTPFile("file1", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file2 = new FTPFile("file2", 0, "remote/", DateTime.now().getMillis(), false);
        FTPFile file3 = new FTPFile("file3", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file4 = new FTPFile("file4", 0, "remote/", DateTime.now().minusDays(2).getMillis(), false);
        FTPFile file5 = new FTPFile("file5", 0, "remote/", DateTime.now().getMillis(), false);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);

        when(mockConnection.listFiles("remote/")).thenReturn(files);

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        verify(mockNextStep).setFilesToDownload(Arrays.asList(file2, file5));
    }

    @Test
    public void ifListingFilesIsUnsuccessfulThenDoNotCallNextStepAndCallBackupStepInstead() {

        ScheduleConfiguration config = new ScheduleConfiguration(null, null, null, 0, null, "remote/", "local/", FileTransferType.FILE);

        when(mockConnection.listFiles("remote/")).thenThrow(new FileListingException());

        ScheduleWorkflow schedule = new ScheduleWorkflow(config);
        schedule.setConnection(mockConnection);

        workflowStep.runStep(schedule);

        assertThat(config.getLastRun()).isEqualTo(new DateTime(0));

        verify(mockNextStep, never()).runStep(schedule);
        verify(mockBackupStep).runStep(schedule);
    }
}
