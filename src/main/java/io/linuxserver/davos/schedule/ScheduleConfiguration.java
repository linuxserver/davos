package io.linuxserver.davos.schedule;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import io.linuxserver.davos.schedule.workflow.actions.PostDownloadAction;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;

public class ScheduleConfiguration {

    private TransferProtocol connectionType;
    private String hostname;
    private int port;
    private UserCredentials credentials;
    private String remoteFilePath;
    private String localFilePath;
    private String scheduleName;
    private List<String> filters = new ArrayList<String>();
    private List<PostDownloadAction> actions = new ArrayList<PostDownloadAction>();
    private DateTime lastRun = DateTime.now();

    public ScheduleConfiguration(final String scheduleName, final TransferProtocol protocol, final String hostname,
            final int port, final UserCredentials credentials, final String remoteFilePath, final String localFilePath) {

        this.scheduleName = scheduleName;
        this.connectionType = protocol;
        this.hostname = hostname;
        this.port = port;
        this.credentials = credentials;
        this.localFilePath = localFilePath;
        this.remoteFilePath = remoteFilePath;
    }

    public TransferProtocol getConnectionType() {
        return connectionType;
    }

    public String getHostName() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public List<PostDownloadAction> getActions() {
        return actions;
    }

    public void setActions(List<PostDownloadAction> actions) {
        this.actions = actions;
    }

    public DateTime getLastRun() {
        return lastRun;
    }

    public void setLastRun(DateTime lastRun) {
        this.lastRun = lastRun;
    }
}
