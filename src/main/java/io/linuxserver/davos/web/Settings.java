package io.linuxserver.davos.web;

import io.linuxserver.davos.web.selectors.LogLevelSelector;

public class Settings {

    private LogLevelSelector logLevel;

    public LogLevelSelector getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevelSelector logLevel) {
        this.logLevel = logLevel;
    }
}
