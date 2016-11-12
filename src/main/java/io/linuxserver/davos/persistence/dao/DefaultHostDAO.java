package io.linuxserver.davos.persistence.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.repository.HostRepository;

@Component
public class DefaultHostDAO implements HostDAO {

    @Resource
    private HostRepository hostRepository;
    
    @Override
    public HostModel saveHost(HostModel host) {
        return hostRepository.save(host);
    }

    @Override
    public HostModel fetchHost(Long id) {
        return hostRepository.findOne(id);
    }

    @Override
    public List<HostModel> fetchAllHosts() {
        return hostRepository.findAll();
    }

    @Override
    public void deleteHost(Long id) {
        hostRepository.delete(id);
    }
}
