package io.linuxserver.davos.bdd;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import io.linuxserver.davos.bdd.helpers.FakeFTPServerFactory;

public class ServerStepDefs {

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
    
    @After("@Server")
    public void after() {
        FakeFTPServerFactory.stop();
    }
}
