package io.linuxserver.davos.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

@Entity
public class HostModel {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @Column
    public String address;

    @Column
    public int port;

    @Column
    public TransferProtocol protocol = TransferProtocol.FTP;

    @Column
    public String username;

    @Column
    public String password;

    @OneToMany(mappedBy = "host", orphanRemoval = false)
    @LazyCollection(LazyCollectionOption.TRUE)
    public List<ScheduleModel> schedules = new ArrayList<ScheduleModel>();

    @Override
    public String toString() {
        return "HostModel [id=" + id + ", name=" + name + ", address=" + address + ", port=" + port + ", protocol=" + protocol
                + ", username=" + username + ", password=" + password + "]";
    }
}
