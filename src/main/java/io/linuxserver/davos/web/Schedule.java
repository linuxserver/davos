package io.linuxserver.davos.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.linuxserver.davos.web.selectors.TransferSelector;

public class Schedule {

    private Long id;
    private String name;
    private int interval = 60;
    private Long host;
    private String hostDirectory;
    private String localDirectory;
    private TransferSelector transferType = TransferSelector.FILE;
    private boolean automatic;
    private String moveFileTo;
    private boolean running;

    private List<String> lastScannedFiles = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private List<Transfer> transfers = new ArrayList<>();
    private List<API> apis = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Long getHost() {
        return host;
    }

    public void setHost(Long host) {
        this.host = host;
    }

    public String getHostDirectory() {
        return hostDirectory;
    }

    public void setHostDirectory(String hostDirectory) {
        this.hostDirectory = hostDirectory;
    }

    public String getLocalDirectory() {
        return localDirectory;
    }

    public void setLocalDirectory(String localDirectory) {
        this.localDirectory = localDirectory;
    }

    public TransferSelector getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferSelector transferType) {
        this.transferType = transferType;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public String getMoveFileTo() {
        return moveFileTo;
    }

    public void setMoveFileTo(String moveFileTo) {
        this.moveFileTo = moveFileTo;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<API> getApis() {
        return apis;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public List<String> getLastScannedFiles() {
        return lastScannedFiles;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }
}
