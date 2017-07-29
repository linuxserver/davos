package io.linuxserver.davos.delegation.services;

import io.linuxserver.davos.Version;
import io.linuxserver.davos.web.selectors.LogLevelSelector;

public interface SettingsService {

    void setLoggingLevel(LogLevelSelector level);
    
    LogLevelSelector getCurrentLoggingLevel();

    Version retrieveRemoteVersion();
}
