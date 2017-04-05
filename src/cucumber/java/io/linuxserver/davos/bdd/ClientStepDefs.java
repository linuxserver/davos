package io.linuxserver.davos.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.linuxserver.davos.bdd.helpers.FakeFTPServerFactory;
import io.linuxserver.davos.bdd.helpers.FakeSFTPServerFactory;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.FTPClient;
import io.linuxserver.davos.transfer.ftp.client.SFTPClient;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;

public class ClientStepDefs {

    private static final String TMP = FileUtils.getTempDirectoryPath();
    
    private Connection connection;
    private Client client;
    private ProgressListener progressListener;

    @After("@Client")
    public void after() {
        client.disconnect();
    }
    
    @When("^davos connects to the server$")
    public void davos_connects_to_the_server() throws Throwable {

        client = new FTPClient();
        client.setCredentials(new UserCredentials("user", "password"));
        client.setHost("localhost");
        client.setPort(FakeFTPServerFactory.getPort());

        connection = client.connect();
    }
    
    @When("^davos connects to the SFTP server$")
    public void davos_connects_to_the_SFTP_server() throws Throwable {

        client = new SFTPClient();
        client.setCredentials(new UserCredentials("user", "password"));
        client.setHost("localhost");
        client.setPort(FakeSFTPServerFactory.getPort());

        connection = client.connect();
    }

    @When("^deletes an SFTP directory$")
    public void deletes_an_SFTP_directory() throws Throwable {
        connection.deleteRemoteFile(new FTPFile("toDelete", 0, "/", 0, true));
    }

    @Then("^the SFTP directory is deleted on the server$")
    public void the_SFTP_directory_is_deleted_on_the_server() throws Throwable {
        assertThat(new File(TMP + "/toDelete").exists()).isFalse();
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
    
    @When("^deletes a directory$")
    public void deletes_a_directory() throws Throwable {
        connection.deleteRemoteFile(new FTPFile("toDelete", 0, "/tmp", 0, true));
    }

    @Then("^the directory is deleted on the server$")
    public void the_directory_is_deleted_on_the_server() throws Throwable {
        assertThat(FakeFTPServerFactory.checkFileExists("/tmp/toDelete")).isFalse();
    }
    
    class CountingFTPProgressListener extends ProgressListener {
        
        int timesCalled;
        
        @Override 
        public void setBytesWritten(long byteCount) {
            super.setBytesWritten(byteCount);
            timesCalled++;
        }
        
        public int getTimesCalled() {
            return timesCalled;
        }
    }
}
