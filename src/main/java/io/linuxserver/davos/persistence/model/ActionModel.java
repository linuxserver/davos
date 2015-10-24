package io.linuxserver.davos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class ActionModel {

    @Id
    @GeneratedValue
    public Long id;
    
    @Column
    public String actionType;
    
    @Column
    public String f1;
    
    @Column
    public String f2;
    
    @Column
    public String f3;
    
    @Column
    public String f4;
    
    @ManyToOne
    @JoinColumn(name = "action_schedule_id")
    public ScheduleConfigurationModel schedule;
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
