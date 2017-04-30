package io.linuxserver.davos.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.Version;

public class VersionChecker {

    private static Logger LOGGER = LoggerFactory.getLogger(VersionChecker.class);
    
    private boolean newVersionAvailable;
    private String newVersion;

    public VersionChecker(Version currentVersion, Version remoteVersion) {

        LOGGER.debug("Current version: {}, Remote version: {}", currentVersion, remoteVersion);
        newVersionAvailable = remoteVersion.isNewerThan(currentVersion);
        LOGGER.debug("Remote version is {}newer", newVersionAvailable ? "" : "not ");
        newVersion = remoteVersion.toString();
    }

    public boolean isNewVersionAvailable() {
        return newVersionAvailable;
    }

    public String getNewVersion() {
        return newVersion;
    }
}
