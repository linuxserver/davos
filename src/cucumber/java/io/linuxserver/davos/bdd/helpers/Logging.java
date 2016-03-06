package io.linuxserver.davos.bdd.helpers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import cucumber.api.java.Before;

public class Logging {

    @Before
    public void before() {
        Configurator.setRootLevel(Level.ERROR);
        Configurator.setLevel("io.linuxserver", Level.ERROR);
    }
}
