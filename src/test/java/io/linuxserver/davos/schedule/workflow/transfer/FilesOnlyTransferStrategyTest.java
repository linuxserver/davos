package io.linuxserver.davos.schedule.workflow.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;

public class FilesOnlyTransferStrategyTest {

    private FilesOnlyTransferStrategy strategy;

    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() {

        initMocks(this);

        strategy = new FilesOnlyTransferStrategy(mockConnection);
    }

    @Test
    public void strategyShouldCallDownloadMethodForFiles() {

        FTPFile file = new FTPFile("file1", 0, "remotePath/", 0, false);

        strategy.transferFile(new FTPTransfer(file), "destination");

        verify(mockConnection).download(file, "destination/");
    }

    @Test
    public void strategyShouldNotCallDownloadMethodForDirectories() {

        FTPFile file = new FTPFile("file1", 0, "remotePath/", 0, true);

        strategy.transferFile(new FTPTransfer(file), "destination");

        verify(mockConnection, never()).download(any(FTPFile.class), anyString());
    }

    @Test
    public void shouldNullifyListenerIfTransferIsForAFolder() {

        FTPFile file = new FTPFile("file1", 0, "remotePath/", 0, true);
        FTPTransfer transfer = new FTPTransfer(file);
        transfer.setListener(new ProgressListener());
        
        
        assertThat(transfer.getListener()).isNotNull();
        strategy.transferFile(transfer, "destination");
        assertThat(transfer.getListener()).isNull();
    }
}
