package io.linuxserver.davos.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.FTPClient;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.progress.FTPProgressListener;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;

public class ClientStepDefs {

    private static final String TMP = FileUtils.getTempDirectoryPath();
    
    private FakeFtpServer fakeFtpServer;
    private int fakeFtpServerPort;
    private Connection connection;
    private Client client;
    private ProgressListener progressListener;

    @After("@Client")
    public void after() {

        client.disconnect();
        fakeFtpServer.stop();
    }

    @Given("^there is an FTP server running$")
    public void there_is_an_FTP_server_running() throws Throwable {

        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/tmp"));
        fakeFtpServer.setServerControlPort(0);

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/tmp"));
        fileSystem.add(new FileEntry("/tmp/file1.txt", "hello world"));
        fileSystem.add(new FileEntry("/tmp/file2.txt", "hello world"));
        fileSystem.add(new FileEntry("/tmp/file3.txt", "hello world"));

        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();

        fakeFtpServerPort = fakeFtpServer.getServerControlPort();
    }

    @When("^davos connects to the server$")
    public void davos_connects_to_the_server() throws Throwable {

        client = new FTPClient();
        client.setCredentials(new UserCredentials("user", "password"));
        client.setHost("localhost");
        client.setPort(fakeFtpServerPort);

        connection = client.connect();
    }

    @Then("^listing the files will show the correct files$")
    public void listing_the_files_will_show_the_correct_files() throws Throwable {

        List<FTPFile> files = connection.listFiles("/tmp");

        assertThat(files).hasSize(3);
        assertThat(files.get(0).getName()).isEqualTo("file3.txt");
        assertThat(files.get(1).getName()).isEqualTo("file2.txt");
        assertThat(files.get(2).getName()).isEqualTo("file1.txt");
    }

    @When("^downloads a file$")
    public void downloads_a_file() throws Throwable {
        connection.download(new FTPFile("file2.txt", "hello world".getBytes().length, "/tmp/", 0, false), TMP);
    }

    @Then("^the file is located in the specified local directory$")
    public void the_file_is_located_in_the_specified_local_directory() throws Throwable {
        
        File file = new File(TMP + "/file2.txt");
        assertThat(file.exists()).isTrue();
        file.delete();
    }
    
    @When("^initialises a Progress Listener for that connection$")
    public void initialises_a_Progress_Listener_for_that_connection() throws Throwable {
        
        progressListener = new CountingFTPProgressListener();
        
        connection.setProgressListener(progressListener);
    }

    @Then("^the Progress Listener will have its values updated$")
    public void the_Progress_Listener_will_have_its_values_updated() throws Throwable {
        
        assertThat(progressListener.getProgress()).isEqualTo(100);
        assertThat(((CountingFTPProgressListener) progressListener).getTimesCalled()).isEqualTo(11);
    }
    
    class CountingFTPProgressListener extends FTPProgressListener {
        
        int timesCalled;
        
        @Override 
        public void updateBytesWritten(long byteCount) {
            super.updateBytesWritten(byteCount);
            timesCalled++;
        }
        
        public int getTimesCalled() {
            return timesCalled;
        }
    }
}
