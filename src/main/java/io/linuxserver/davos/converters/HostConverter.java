package io.linuxserver.davos.converters;

import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;
import io.linuxserver.davos.web.Host;
import io.linuxserver.davos.web.selectors.ProtocolSelector;

@Component
public class HostConverter implements Converter<HostModel, Host> {

    @Override
    public Host convertTo(HostModel source) {

        Host host = new Host();
        
        host.setId(source.id);
        host.setName(source.name);
        host.setAddress(source.address);
        host.setPort(source.port);
        host.setUsername(source.username);
        host.setPassword(source.password);
        host.setProtocol(ProtocolSelector.valueOf(source.protocol.toString()));
        host.setIdentityFileEnabled(source.isIdentityFileEnabled());
        host.setIdentityFile(source.identityFile);
        
        return host;
    }

    @Override
    public HostModel convertFrom(Host source) {

        HostModel model = new HostModel();
        
        model.id = source.getId();
        model.name = source.getName();
        model.address = source.getAddress();
        model.port = source.getPort();
        model.username = source.getUsername();
        model.password = source.getPassword();
        model.protocol = TransferProtocol.valueOf(source.getProtocol().toString());
        model.setIdentityFileEnabled(source.isIdentityFileEnabled());
        model.identityFile = source.getIdentityFile();
        
        return model;
    }
}
