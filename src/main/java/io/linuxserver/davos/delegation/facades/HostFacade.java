package io.linuxserver.davos.delegation.facades;

import java.util.List;

import io.linuxserver.davos.dto.HostDTO;

public interface HostFacade {

    List<HostDTO> fetchAllHosts();
    
    HostDTO fetchHost(Long id);
    
    HostDTO saveHost(HostDTO host);
    
    void deleteHost(Long id);

    HostDTO createBlankHost();
}
