package io.linuxserver.davos.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    public ScheduleModel schedule;

    @Override
    public String toString() {
        return "ActionModel [id=" + id + ", actionType=" + actionType + ", f1=" + f1 + ", f2=" + f2 + ", f3=" + f3 + ", f4=" + f4
                + "]";
    }
}
