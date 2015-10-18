package io.linuxserver.davos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

@Entity
public class ScheduleConfigurationModel {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @Column
    public boolean startAutomatically;

    @Column
    public int interval;

    @Column
    public TransferProtocol connectionType;

    @Column
    public String hostName;

    @Column
    public int port;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String remoteFilePath;

    @Column
    public String localFilePath;

    @Column
    public DateTime lastRun;

    @Column
    public FileTransferType transferType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL)
    public List<FilterModel> filters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL)
    public List<ActionModel> actions;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
