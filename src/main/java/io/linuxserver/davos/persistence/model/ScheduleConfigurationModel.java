package io.linuxserver.davos.persistence.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    public Date lastRun;

    @Column
    public FileTransferType transferType;

    @OneToMany(orphanRemoval = true, mappedBy = "schedule", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<FilterModel> filters = new ArrayList<FilterModel>();

    @OneToMany(orphanRemoval = true, mappedBy = "schedule", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<ActionModel> actions = new ArrayList<ActionModel>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
