package io.linuxserver.davos.delegation.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.linuxserver.davos.converters.HostConverter;
import io.linuxserver.davos.persistence.dao.HostDAO;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.web.Host;

@Component
public class HostServiceImpl implements HostService {

    @Resource
    private HostDAO hostDAO;
    
    @Resource
    private ScheduleDAO scheduleDAO;
    
    @Resource
    private HostConverter hostConverter;
    
    @Override
    public Host fetchHost(Long id) {
        return toHost(hostDAO.fetchHost(id));
    }

    @Override
    public Host saveHost(Host host) {
        
        HostModel model = hostConverter.convertFrom(host);
        return hostConverter.convertTo(hostDAO.saveHost(model));
    }

    @Override
    public void deleteHost(Long id) {
        hostDAO.deleteHost(id);
    }

    @Override
    public List<Host> fetchAllHosts() {
        return hostDAO.fetchAllHosts().stream().map(this::toHost).collect(Collectors.toList());
    }
    
    private Host toHost(HostModel model) {
        return hostConverter.convertTo(model);
    }

    @Override
    public List<Long> fetchSchedulesUsingHost(Long id) {
        return scheduleDAO.fetchSchedulesUsingHost(id).stream().map(s -> s.id).collect(Collectors.toList());
    }
}
