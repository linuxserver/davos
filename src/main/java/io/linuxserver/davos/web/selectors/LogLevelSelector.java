package io.linuxserver.davos.web.selectors;

public enum LogLevelSelector {
    
    DEBUG, INFO, WARN, ERROR;

    public static final LogLevelSelector[] ALL = { DEBUG, INFO, WARN, ERROR };
}
