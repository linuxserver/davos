package io.linuxserver.davos.delegation.services;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.logging.LoggingManager;
import io.linuxserver.davos.web.selectors.LogLevelSelector;

@Component
public class SettingsServiceImpl implements SettingsService {

    private LogLevelSelector currentLevel = LogLevelSelector.INFO;
    
    @Override
    public void setLoggingLevel(LogLevelSelector level) {
        currentLevel = level;
        LoggingManager.setLogLevel(Level.valueOf(level.toString()));
    }

    @Override
    public LogLevelSelector getCurrentLoggingLevel() {
        return currentLevel;
    }
}
