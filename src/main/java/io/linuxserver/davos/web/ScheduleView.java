package io.linuxserver.davos.web;

import java.util.ArrayList;
import java.util.List;

import io.linuxserver.davos.web.selectors.TransferSelector;

public class ScheduleView {

    private Long id;
    private String name;
    private int interval = 60;
    private Long host;
    private String hostDirectory;
    private String localDirectory;
    private TransferSelector transferType = TransferSelector.FILES;
    private boolean automatic;
    private String moveFileTo;

    private List<FilterView> filters = new ArrayList<>();
    private List<NotificationView> notifications = new ArrayList<>();
    private List<APIView> apis = new ArrayList<>();

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

    public List<FilterView> getFilters() {
        return filters;
    }

    public String getMoveFileTo() {
        return moveFileTo;
    }

    public void setMoveFileTo(String moveFileTo) {
        this.moveFileTo = moveFileTo;
    }

    public List<NotificationView> getNotifications() {
        return notifications;
    }

    public List<APIView> getApis() {
        return apis;
    }
}
