package io.linuxserver.davos.delegation.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.converters.HostConverter;
import io.linuxserver.davos.exception.HostInUseException;
import io.linuxserver.davos.persistence.dao.HostDAO;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.transfer.ftp.client.Client;
import io.linuxserver.davos.transfer.ftp.client.ClientFactory;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials.Identity;
import io.linuxserver.davos.web.Host;

@Component
public class HostServiceImpl implements HostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostServiceImpl.class);

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
        
        List<Long> schedulesUsingHost = fetchSchedulesUsingHost(id);
        
        if (schedulesUsingHost.isEmpty()) {
            hostDAO.deleteHost(id);
        } else {
            throw new HostInUseException("Host is being used by Schedules: " + schedulesUsingHost);
        }
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

    @Override
    public void testConnection(Host host) {

        HostModel model = hostConverter.convertFrom(host);

        LOGGER.info("Attempting to test connection to host", model.address);

        Client client = new ClientFactory().getClient(model.protocol);

        LOGGER.debug("Credentials: {} : {}", model.username, model.password);
        
        UserCredentials userCredentials;
        
        if (model.isIdentityFileEnabled())
            userCredentials = new UserCredentials(model.username, new Identity(model.identityFile));
        else
            userCredentials = new UserCredentials(model.username, model.password);
        
        client.setCredentials(userCredentials);
        client.setHost(model.address);
        client.setPort(model.port);

        LOGGER.debug("Making connection on port {}", model.port);
        client.connect();
        LOGGER.info("Connection successful.");
        client.disconnect();
        LOGGER.debug("Disconnected");
    }
}
