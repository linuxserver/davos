package io.linuxserver.davos.persistence.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    public String connectionType;
    
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
    public String transferType;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    public List<FilterModel> filters;
}
