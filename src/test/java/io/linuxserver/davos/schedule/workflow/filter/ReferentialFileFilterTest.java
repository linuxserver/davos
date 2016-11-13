package io.linuxserver.davos.schedule.workflow.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class ReferentialFileFilterTest {

    @Test
    public void shouldReturnAllFTPFilesIfLastScanIsEmpty() {
        
        List<FTPFile> newFiles = new ArrayList<>();
        newFiles.add(new FTPFile("file1", 0, "", 0, false));
        newFiles.add(new FTPFile("file2", 0, "", 0, false));
        newFiles.add(new FTPFile("file3", 0, "", 0, false));
        
        List<String> oldFiles = new ArrayList<>();
        
        List<FTPFile> filteredFiles = new ReferentialFileFilter(oldFiles).filter(newFiles);
        
        assertThat(filteredFiles).hasSize(3);
        assertThat(filteredFiles.get(0).getName()).isEqualTo("file1");
        assertThat(filteredFiles.get(1).getName()).isEqualTo("file2");
        assertThat(filteredFiles.get(2).getName()).isEqualTo("file3");
    }
    
    @Test
    public void shouldReturnFilteredFTPFilesIfLastScanIsMissingFiles() {
        
        List<FTPFile> newFiles = new ArrayList<>();
        newFiles.add(new FTPFile("file1", 0, "", 0, false));
        newFiles.add(new FTPFile("file2", 0, "", 0, false));
        newFiles.add(new FTPFile("file3", 0, "", 0, false));
        
        List<String> oldFiles = Arrays.asList("file1", "file3");
        
        List<FTPFile> filteredFiles = new ReferentialFileFilter(oldFiles).filter(newFiles);
        
        assertThat(filteredFiles).hasSize(1);
        assertThat(filteredFiles.get(0).getName()).isEqualTo("file2");
    }
    
    @Test
    public void shouldReturnEmptyListIfNewFilesAreEmpty() {

        List<FTPFile> newFiles = new ArrayList<>();
        List<String> oldFiles = Arrays.asList("file1", "file3");
        
        List<FTPFile> filteredFiles = new ReferentialFileFilter(oldFiles).filter(newFiles);
        
        assertThat(filteredFiles).isEmpty();
    }
}
