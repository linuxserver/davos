package io.linuxserver.davos.delegation.facades;

import java.util.List;

import org.springframework.stereotype.Component;

import io.linuxserver.davos.dto.HostDTO;

@Component
public class HostFacadeImpl implements HostFacade {

    @Override
    public HostDTO fetchHost(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HostDTO saveHost(HostDTO host) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteHost(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<HostDTO> fetchAllHosts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HostDTO createBlankHost() {
        return new HostDTO();
    }
}
