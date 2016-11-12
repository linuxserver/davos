package io.linuxserver.davos.persistence.dao;

import java.util.List;

import io.linuxserver.davos.persistence.model.HostModel;

public interface HostDAO {

    HostModel saveHost(HostModel host);
    
    HostModel fetchHost(Long id);
    
    List<HostModel> fetchAllHosts();
    
    void deleteHost(Long id);
}
