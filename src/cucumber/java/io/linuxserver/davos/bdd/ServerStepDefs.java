package io.linuxserver.davos.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import io.linuxserver.davos.bdd.helpers.FakeFTPServerFactory;
import io.linuxserver.davos.bdd.helpers.FakeSFTPServerFactory;

public class ServerStepDefs {

    private static final String TMP = FileUtils.getTempDirectoryPath();
    
    @Given("^there is an FTP server running$")
    public void there_is_an_FTP_server_running() throws Throwable {
        FakeFTPServerFactory.setup();
    }
    
    @Given("^the FTP server has a directory with contents$")
    public void the_FTP_server_has_a_directory_with_contents() throws Throwable {
        FakeFTPServerFactory.addDirectoryWithNameAndNumberOfFiles("toDelete", 3);
    }
    
    @Given("^the FTP server has a directory without contents$")
    public void the_FTP_server_has_a_directory_without_contents() throws Throwable {
        FakeFTPServerFactory.addDirectoryWithNameAndNumberOfFiles("toDelete", 0);
    }
    
    @Given("^there is an SFTP server running$")
    public void there_is_an_SFTP_server_running() throws Throwable {
        FakeSFTPServerFactory.setup();
    }
    
    @Given("^the SFTP server has a directory with contents$")
    public void the_SFTP_server_has_a_directory_with_contents() throws Throwable {
        
        FakeSFTPServerFactory.addDirectoryWithNameAndNumberOfFiles("toDelete", 3);
        
        assertThat(new File(TMP + "/toDelete").exists()).isTrue();
        assertThat(new File(TMP + "/toDelete/file0").exists()).isTrue();
        assertThat(new File(TMP + "/toDelete/file1").exists()).isTrue();
        assertThat(new File(TMP + "/toDelete/file2").exists()).isTrue();
    }
    
    @Given("^the SFTP server has a directory without contents$")
    public void the_SFTP_server_has_a_directory_without_contents() throws Throwable {
        FakeSFTPServerFactory.addDirectoryWithNameAndNumberOfFiles("toDelete", 0);
    }
    
    @After("@Server")
    public void after() {
        FakeFTPServerFactory.stop();
    }
    
    @After("@SFTPServer")
    public void afterSFTP() throws IOException {
        FakeSFTPServerFactory.stop();
    }
}
