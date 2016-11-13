package io.linuxserver.davos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ScannedFileModel {

    @Id
    @GeneratedValue
    public Long id;
    
    @Column
    public String file;
    
    @ManyToOne
    @JoinColumn(name = "scanned_file_schedule_id")
    public ScheduleModel schedule;
}
