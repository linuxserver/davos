package io.linuxserver.davos.bdd;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import io.linuxserver.davos.bdd.helpers.FakeFTPServerFactory;

public class ServerStepDefs {

    @Given("^there is an FTP server running$")
    public void there_is_an_FTP_server_running() throws Throwable {
        FakeFTPServerFactory.setup();
    }
    
    @After("@Server")
    public void after() {
        FakeFTPServerFactory.stop();
    }
}
