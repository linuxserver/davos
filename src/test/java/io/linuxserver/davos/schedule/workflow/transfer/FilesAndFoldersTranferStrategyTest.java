package io.linuxserver.davos.schedule.workflow.transfer;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class FilesAndFoldersTranferStrategyTest {

    private FilesAndFoldersTranferStrategy strategy;
    
    @Mock
    private Connection mockConnection;
    
    @Before
    public void setUp() {
        
        initMocks(this);
        
        strategy = new FilesAndFoldersTranferStrategy(mockConnection);
    }
    
    @Test
    public void strategyShouldCallDownloadMethodForFiles() {
        
        FTPFile file = new FTPFile("file1", 0, "remotePath/", 0, false);
        
        strategy.transferFile(new FTPTransfer(file), "destination");
        
        verify(mockConnection).download(file, "destination/");
    }
    
    @Test
    public void strategyShouldCallDownloadMethodForDirectories() {
        
        FTPFile file = new FTPFile("file1", 0, "remotePath/", 0, true);
        
        strategy.transferFile(new FTPTransfer(file), "destination");
        
        verify(mockConnection).download(file, "destination/");
    }
}
