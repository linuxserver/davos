package io.linuxserver.davos.schedule.workflow.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.linuxserver.davos.schedule.workflow.actions.PostDownloadAction;
import io.linuxserver.davos.schedule.workflow.actions.PostDownloadExecution;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class TransferStrategyTest {

    @Test
    public void toStringShouldPrintClassName() {
        assertThat(new TestTransferStrategy(null).toString()).isEqualTo("TestTransferStrategy");
        assertThat(new AnotherTestTransferStrategy(null).toString()).isEqualTo("AnotherTestTransferStrategy");
    }

    @Test
    public void runPostDownloadActionShouldCallAllGivenActionsWithTheFile() {

        ArgumentCaptor<PostDownloadExecution> captor = ArgumentCaptor.forClass(PostDownloadExecution.class);

        DownloadActionImplTestTransferStrategy strategy = new DownloadActionImplTestTransferStrategy(null);

        PostDownloadAction mockAction1 = mock(PostDownloadAction.class);
        PostDownloadAction mockAction2 = mock(PostDownloadAction.class);

        strategy.setPostDownloadActions(Arrays.asList(mockAction1, mockAction2));

        strategy.transferFile(new FTPTransfer(new FTPFile("file1", 0, null, 0, false)), "destination/");

        verify(mockAction1).execute(captor.capture());
        verify(mockAction2).execute(captor.capture());

        assertThat(captor.getAllValues().get(0).fileName).isEqualTo("file1");
        assertThat(captor.getAllValues().get(1).fileName).isEqualTo("file1");
    }

    @Test
    public void ensureNulLActionsAreCheckedBeforeAttemptingToRun() {
        
        DownloadActionImplTestTransferStrategy strategy = new DownloadActionImplTestTransferStrategy(null);
        strategy.setPostDownloadActions(null);
        strategy.transferFile(new FTPTransfer(new FTPFile("file1", 0, null, 0, false)), "destination/");
    }

    class TestTransferStrategy extends TransferStrategy {

        public TestTransferStrategy(Connection connection) {
            super(connection);
        }

        @Override
        public void transferFile(FTPTransfer fileToTransfer, String destination) {
        }
    }

    class AnotherTestTransferStrategy extends TransferStrategy {

        public AnotherTestTransferStrategy(Connection connection) {
            super(connection);
        }

        @Override
        public void transferFile(FTPTransfer fileToTransfer, String destination) {
        }
    }

    class DownloadActionImplTestTransferStrategy extends TransferStrategy {

        public DownloadActionImplTestTransferStrategy(Connection connection) {
            super(connection);
        }

        @Override
        public void transferFile(FTPTransfer fileToTransfer, String destination) {
            runPostDownloadAction(fileToTransfer.getFile());
        }
    }
}
