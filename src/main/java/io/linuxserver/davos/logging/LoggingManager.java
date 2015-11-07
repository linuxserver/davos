package io.linuxserver.davos.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingManager.class);
    
    public static void enableDebug() {
        Configurator.setLevel("io.linuxserver", Level.DEBUG);
        LOGGER.debug("DEBUG has been enabled");
    }
    
    public static void disableDebug() {
        Configurator.setLevel("io.linuxserver", Level.INFO);
        LOGGER.info("DEBUG has been disabled. Back at INFO.");
    }
}
