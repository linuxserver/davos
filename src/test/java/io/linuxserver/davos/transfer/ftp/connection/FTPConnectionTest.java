package io.linuxserver.davos.transfer.ftp.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.CountingOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;
import io.linuxserver.davos.transfer.ftp.exception.DownloadFailedException;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;
import io.linuxserver.davos.transfer.ftp.exception.FileListingException;
import io.linuxserver.davos.util.FileStreamFactory;
import io.linuxserver.davos.util.FileUtils;

public class FTPConnectionTest {

    private static final String LOCAL_DIRECTORY = ".";
    private static final String DIRECTORY_PATH = "this/is/a/directory";

    @InjectMocks
    private FTPConnection ftpConnection;

    @Mock
    private FileStreamFactory mockFileStreamFactory;

    @Mock
    private FileUtils mockFileUtils;

    @Mock
    private FileOutputStream mockFileOutputStream;

    private org.apache.commons.net.ftp.FTPClient mockFtpClient;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws IOException {

        mockFtpClient = mock(org.apache.commons.net.ftp.FTPClient.class);

        when(mockFtpClient.changeWorkingDirectory(anyString())).thenReturn(true);
        when(mockFtpClient.printWorkingDirectory()).thenReturn(DIRECTORY_PATH);
        when(mockFtpClient.retrieveFile(anyString(), any(OutputStream.class))).thenReturn(true);
        when(mockFtpClient.deleteFile(any(String.class))).thenReturn(true);

        org.apache.commons.net.ftp.FTPFile[] files = createRemoteFTPFiles();

        ftpConnection = new FTPConnection(mockFtpClient);

        initMocks(this);

        when(mockFtpClient.listFiles(anyString())).thenReturn(files);
        when(mockFileStreamFactory.createOutputStream("./remote.file")).thenReturn(mockFileOutputStream);
    }

    @Test
    public void whenListingFilesThenFtpClientListFilesMethodShouldBeCalledForCurrentWorkingDirectory() throws IOException {

        ftpConnection.listFiles();

        verify(mockFtpClient).listFiles("this/is/a/directory/");
    }

    @Test
    public void ifWhenListingFilesFtpClientThrowsExceptionThenCatchAndRethrowFileListingExcepton() throws IOException {

        expectedException.expect(FileListingException.class);
        expectedException.expectMessage(is(equalTo("Unable to list files in directory " + DIRECTORY_PATH)));

        when(mockFtpClient.listFiles("this/is/a/directory/")).thenThrow(new IOException());

        ftpConnection.listFiles();
    }

    @Test
    public void whenListingFilesThenFileArrayThatListFilesReturnsShouldBeConvertedToListOfFtpFilesAndReturned()
            throws IOException {

        List<FTPFile> returnedFiles = ftpConnection.listFiles();

        assertThat(returnedFiles.get(0).getName()).isEqualTo("File 1");
        assertThat(returnedFiles.get(0).getSize()).isEqualTo(1000l);
        assertThat(returnedFiles.get(0).getPath()).isEqualTo("this/is/a/directory/");
        assertThat(returnedFiles.get(0).isDirectory()).isFalse();

        assertThat(returnedFiles.get(1).getName()).isEqualTo("File 2");
        assertThat(returnedFiles.get(1).getSize()).isEqualTo(2000l);
        assertThat(returnedFiles.get(1).getPath()).isEqualTo("this/is/a/directory/");
        assertThat(returnedFiles.get(1).isDirectory()).isTrue();

        assertThat(returnedFiles.get(2).getName()).isEqualTo("File 3");
        assertThat(returnedFiles.get(2).getSize()).isEqualTo(3000l);
        assertThat(returnedFiles.get(2).getPath()).isEqualTo("this/is/a/directory/");
        assertThat(returnedFiles.get(2).isDirectory()).isFalse();
    }

    @Test
    public void returnedFtpFilesShouldHaveCorrectModifiedDateTimesAgainstThem() {

        List<FTPFile> files = ftpConnection.listFiles();

        assertThat(files.get(0).getLastModified().toString("dd/MM/yyyy HH:mm:ss")).isEqualTo("19/03/2014 21:40:00");
        assertThat(files.get(1).getLastModified().toString("dd/MM/yyyy HH:mm:ss")).isEqualTo("19/03/2014 21:40:00");
        assertThat(files.get(2).getLastModified().toString("dd/MM/yyyy HH:mm:ss")).isEqualTo("19/03/2014 21:40:00");
    }

    @Test
    public void whenListingFilesAndGivingRelativePathThenThatPathShouldBeUsedAlongsideCurrentWorkingDir() throws IOException {

        ftpConnection.listFiles("relativePath");

        verify(mockFtpClient).listFiles("relativePath/");
    }

    @Test
    public void downloadMethodShouldCreateLocalFileStreamFromCorrectPathBasedOnRemoteFileName() throws FileNotFoundException {

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);
        ftpConnection.download(file, LOCAL_DIRECTORY);

        verify(mockFileStreamFactory).createOutputStream(LOCAL_DIRECTORY + "/remote.file");
    }

    @Test
    public void downloadMethodShouldCreateLocalFileStreamContainingProgressListener() throws IOException {

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);

        ftpConnection.setProgressListener(new ProgressListener());
        ftpConnection.download(file, LOCAL_DIRECTORY);

        verify(mockFileStreamFactory).createOutputStream(LOCAL_DIRECTORY + "/remote.file");
        verify(mockFtpClient).retrieveFile(eq("path/to/remote.file"), any(CountingOutputStream.class));
    }

    @Test
    public void downloadMethodShouldCallOnFtpClientRetrieveFilesMethodWithRemoteFilename() throws IOException {

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);
        ftpConnection.download(file, LOCAL_DIRECTORY);

        verify(mockFtpClient).retrieveFile("path/to/remote.file", mockFileOutputStream);
    }

    @Test
    public void downloadMethodShouldThrowExceptionIfUnableToOpenStreamToLocalFile() throws IOException {

        expectedException.expect(DownloadFailedException.class);
        expectedException.expectMessage(is(equalTo("Unable to write to local directory " + LOCAL_DIRECTORY + "/remote.file")));

        when(mockFtpClient.retrieveFile("path/to/remote.file", mockFileOutputStream)).thenThrow(new FileNotFoundException());

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);
        ftpConnection.download(file, LOCAL_DIRECTORY);
    }

    @Test
    public void shouldDownloadFailForAnyReasonWhileInProgressThenCatchIOExceptionAndThrowNewDownloadFailedException()
            throws IOException {

        expectedException.expect(DownloadFailedException.class);
        expectedException.expectMessage(is(equalTo("Unable to download file path/to/remote.file")));

        when(mockFtpClient.retrieveFile("path/to/remote.file", mockFileOutputStream)).thenThrow(new IOException());

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);
        ftpConnection.download(file, LOCAL_DIRECTORY);
    }

    @Test
    public void ifRetrieveFileMethodInClientReturnsFalseThenThrowDownloadFailedException() throws IOException {

        expectedException.expect(DownloadFailedException.class);
        expectedException.expectMessage(is(equalTo("Server returned failure while downloading.")));

        when(mockFtpClient.retrieveFile("path/to/remote.file", mockFileOutputStream)).thenReturn(false);

        FTPFile file = new FTPFile("remote.file", 0l, "path/to", 0, false);
        ftpConnection.download(file, LOCAL_DIRECTORY);
    }

    @Test
    public void printingWorkingDirectoryShouldCallOnUnderlyingClientMethodToGetCurrentDirectory() throws IOException {

        ftpConnection.currentDirectory();

        verify(mockFtpClient).printWorkingDirectory();
    }

    @Test
    public void printingWorkingDirectoryShouldReturnExactlyWhatTheUnderlyingClientReturns() {
        assertThat(ftpConnection.currentDirectory()).isEqualTo(DIRECTORY_PATH);
    }

    @Test
    public void ifClientThrowsExceptionWhenTryingToGetWorkingDirectoryThenCatchExceptionAndRethrow() throws IOException {

        expectedException.expect(FileListingException.class);
        expectedException.expectMessage(is(equalTo("Unable to print the working directory")));

        when(mockFtpClient.printWorkingDirectory()).thenThrow(new IOException());

        ftpConnection.currentDirectory();
    }

    @Test
    public void shouldDeleteRemoteFile() throws IOException {

        FTPFile file = new FTPFile("file.name", 0, "/some/directory", 0, false);

        ftpConnection.deleteRemoteFile(file);

        verify(mockFtpClient).deleteFile("/some/directory/file.name");
    }

    @Test
    public void ifDeleteFailsThenExceptionShouldBeThrown() throws IOException {

        expectedException.expect(FTPException.class);
        expectedException.expectMessage(equalTo("Unable to delete file on remote server"));
        
        FTPFile file = new FTPFile("file.name", 0, "/some/directory", 0, false);

        when(mockFtpClient.deleteFile(anyString())).thenReturn(false);
        ftpConnection.deleteRemoteFile(file);
    }
    
    @Test
    public void ifDeleteThrowsExceptionItShouldBeCaughtAndRethrown() throws IOException {
        
        expectedException.expect(FTPException.class);
        expectedException.expectMessage(equalTo("Unable to delete file on remote server"));
        
        FTPFile file = new FTPFile("file.name", 0, "/some/directory", 0, false);

        when(mockFtpClient.deleteFile(anyString())).thenThrow(new IOException());
        ftpConnection.deleteRemoteFile(file);
    }

    @Test
    public void downloadShouldRecursivelyCheckFileIfFolderThenLsThatAndGetOnlyFiles() throws IOException {

        initRecursiveListings();

        FileOutputStream stream1 = mock(FileOutputStream.class);
        FileOutputStream stream2 = mock(FileOutputStream.class);
        FileOutputStream stream3 = mock(FileOutputStream.class);
        FileOutputStream stream4 = mock(FileOutputStream.class);
        FileOutputStream stream5 = mock(FileOutputStream.class);
        FileOutputStream stream6 = mock(FileOutputStream.class);

        when(mockFileStreamFactory.createOutputStream("some/directory/folder/file1.txt")).thenReturn(stream1);
        when(mockFileStreamFactory.createOutputStream("some/directory/folder/file2.txt")).thenReturn(stream2);
        when(mockFileStreamFactory.createOutputStream("some/directory/folder/directory1/file3.txt")).thenReturn(stream3);
        when(mockFileStreamFactory.createOutputStream("some/directory/folder/directory1/directory2/file5.txt"))
                .thenReturn(stream4);
        when(mockFileStreamFactory.createOutputStream("some/directory/folder/directory1/directory2/file6.txt"))
                .thenReturn(stream5);
        when(mockFileStreamFactory.createOutputStream("some/directory/folder/directory1/file4.txt")).thenReturn(stream6);

        FTPFile directory = new FTPFile("folder", 0, "path/to", 0, true);
        ftpConnection.download(directory, "some/directory");

        verify(mockFileUtils).createLocalDirectory("some/directory/folder/");
        verify(mockFtpClient).listFiles("path/to/folder/");

        verify(mockFileUtils).createLocalDirectory("some/directory/folder/directory1/");
        verify(mockFtpClient).listFiles("path/to/folder/directory1/");

        verify(mockFileUtils).createLocalDirectory("some/directory/folder/directory1/directory2/");
        verify(mockFtpClient).listFiles("path/to/folder/directory1/directory2/");

        InOrder inOrder = Mockito.inOrder(mockFtpClient, stream1, stream2, stream3, stream4, stream5, stream6);

        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/file1.txt", stream1);
        inOrder.verify(stream1).close();
        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/file2.txt", stream2);
        inOrder.verify(stream2).close();
        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/directory1/file3.txt", stream3);
        inOrder.verify(stream3).close();
        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/directory1/directory2/file5.txt", stream4);
        inOrder.verify(stream4).close();
        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/directory1/directory2/file6.txt", stream5);
        inOrder.verify(stream5).close();
        inOrder.verify(mockFtpClient).retrieveFile("path/to/folder/directory1/file4.txt", stream6);
        inOrder.verify(stream6).close();
    }

    private void initRecursiveListings() throws IOException {

        org.apache.commons.net.ftp.FTPFile[] entries = new org.apache.commons.net.ftp.FTPFile[5];

        entries[0] = (createSingleEntry(".", 123l, 1394525265, true));
        entries[1] = (createSingleEntry("..", 123l, 1394525265, true));
        entries[2] = (createSingleEntry("file1.txt", 123l, 1394525265, false));
        entries[3] = (createSingleEntry("file2.txt", 456l, 1394652161, false));
        entries[4] = (createSingleEntry("directory1", 789l, 1391879364, true));

        when(mockFtpClient.listFiles("path/to/folder/")).thenReturn(entries);

        org.apache.commons.net.ftp.FTPFile[] subEntries = new org.apache.commons.net.ftp.FTPFile[5];

        subEntries[0] = (createSingleEntry(".", 123l, 1394525265, true));
        subEntries[1] = (createSingleEntry("..", 123l, 1394525265, true));
        subEntries[2] = (createSingleEntry("file3.txt", 789l, 1394525265, false));
        subEntries[3] = (createSingleEntry("directory2", 789l, 1394525265, true));
        subEntries[4] = (createSingleEntry("file4.txt", 789l, 1394525265, false));

        when(mockFtpClient.listFiles("path/to/folder/directory1/")).thenReturn(subEntries);

        org.apache.commons.net.ftp.FTPFile[] subSubEntries = new org.apache.commons.net.ftp.FTPFile[4];

        subSubEntries[0] = (createSingleEntry(".", 123l, 1394525265, true));
        subSubEntries[1] = (createSingleEntry("..", 123l, 1394525265, true));
        subSubEntries[2] = (createSingleEntry("file5.txt", 789l, 1394525265, false));
        subSubEntries[3] = (createSingleEntry("file6.txt", 789l, 1394525265, false));

        when(mockFtpClient.listFiles("path/to/folder/directory1/directory2/")).thenReturn(subSubEntries);
    }

    private org.apache.commons.net.ftp.FTPFile createSingleEntry(String fileName, long size, int mTime, boolean directory) {

        org.apache.commons.net.ftp.FTPFile file = mock(org.apache.commons.net.ftp.FTPFile.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTime));

        when(file.getName()).thenReturn(fileName);
        when(file.getTimestamp()).thenReturn(calendar);
        when(file.getSize()).thenReturn(size);
        when(file.isDirectory()).thenReturn(directory);

        return file;
    }

    private org.apache.commons.net.ftp.FTPFile[] createRemoteFTPFiles() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, 2, 19, 21, 40, 00);

        org.apache.commons.net.ftp.FTPFile[] files = new org.apache.commons.net.ftp.FTPFile[5];

        org.apache.commons.net.ftp.FTPFile currentDir = mock(org.apache.commons.net.ftp.FTPFile.class);
        when(currentDir.getName()).thenReturn(".");
        when(currentDir.getTimestamp()).thenReturn(calendar);

        org.apache.commons.net.ftp.FTPFile parentDir = mock(org.apache.commons.net.ftp.FTPFile.class);
        when(parentDir.getName()).thenReturn("..");
        when(parentDir.getTimestamp()).thenReturn(calendar);

        files[0] = currentDir;
        files[1] = parentDir;

        for (int i = 2; i < 5; i++) {

            org.apache.commons.net.ftp.FTPFile file = mock(org.apache.commons.net.ftp.FTPFile.class);

            when(file.getName()).thenReturn("File " + (i - 1));
            when(file.getSize()).thenReturn((long) (i - 1) * 1000);
            when(file.getTimestamp()).thenReturn(calendar);
            when(file.isDirectory()).thenReturn(setTrueIfNumberIsEven(i));

            files[i] = file;
        }

        return files;
    }

    private boolean setTrueIfNumberIsEven(int i) {
        return (i + 1) % 2 == 0 ? true : false;
    }
}