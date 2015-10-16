package io.linuxserver.davos.schedule.workflow.actions;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.linuxserver.davos.util.FileUtils;

public class MoveFileActionTest {

    @InjectMocks
    private MoveFileAction moveFileAction = new MoveFileAction("oldPath", "newPath");
    
    @Mock
    private FileUtils mockFileUtils;
    
    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @Test
    public void executeShouldMoveTheFile() throws IOException {
        
        PostDownloadExecution execution = new PostDownloadExecution();
        execution.fileName = "filename";
        
        moveFileAction.execute(execution);
        
        verify(mockFileUtils).moveFileToDirectory("oldPath/filename", "newPath/");
    }
    
    @Test
    public void ifMovingOfFileFailsThenDoNotPerpetuateError() throws IOException {
        
        doThrow(new IOException()).when(mockFileUtils).moveFileToDirectory(anyString(), anyString());
        
        PostDownloadExecution execution = new PostDownloadExecution();
        execution.fileName = "filename";
        
        moveFileAction.execute(execution);
    }
}
