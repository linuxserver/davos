package io.linuxserver.davos.delegation.services;

import java.util.List;

import io.linuxserver.davos.web.Host;

public interface HostService {

    List<Host> fetchAllHosts();
    
    Host fetchHost(Long id);
    
    Host saveHost(Host host);
    
    void deleteHost(Long id);
    
    List<Long> fetchSchedulesUsingHost(Long id);
}
