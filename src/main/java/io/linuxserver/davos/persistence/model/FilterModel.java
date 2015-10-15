package io.linuxserver.davos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FilterModel {

    @Id
    @GeneratedValue
    public Long id;
    
    @Column
    public String value;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public ScheduleConfigurationModel schedule;
}
