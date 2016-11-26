package io.linuxserver.davos.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import io.linuxserver.davos.transfer.ftp.FileTransferType;

@Entity
public class ScheduleModel {

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
    public String remoteFilePath;

    @Column
    public String localFilePath;
    
    @Column
    public String moveFileTo;
    
    @Column
    public boolean filtersMandatory;

    @Column
    public FileTransferType transferType = FileTransferType.FILE;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_host_id")
    public HostModel host;

    @OneToMany(orphanRemoval = true, mappedBy = "schedule", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<FilterModel> filters = new ArrayList<FilterModel>();

    @OneToMany(orphanRemoval = true, mappedBy = "schedule", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<ActionModel> actions = new ArrayList<ActionModel>();
    
    @OneToMany(orphanRemoval = true, mappedBy = "schedule", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<ScannedFileModel> scannedFiles = new ArrayList<ScannedFileModel>();

    @Override
    public String toString() {
        return "ScheduleModel [id=" + id + ", name=" + name + ", startAutomatically=" + startAutomatically + ", interval="
                + interval + ", remoteFilePath=" + remoteFilePath + ", localFilePath=" + localFilePath + ", transferType="
                + transferType + ", filters=" + filters + ", actions=" + actions + "]";
    }
}
