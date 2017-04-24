package io.linuxserver.davos.web;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.linuxserver.davos.web.selectors.ProtocolSelector;

public class Host {

    private Long id;
    private String name;
    private String address;
    private int port;
    private ProtocolSelector protocol = ProtocolSelector.SFTP;
    private String username;
    private String password;
    private String identityFile;
    private boolean identityFileEnabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ProtocolSelector getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolSelector protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getIdentityFile() {
        return identityFile;
    }

    public void setIdentityFile(String identityFile) {
        this.identityFile = identityFile;
    }

    public boolean isIdentityFileEnabled() {
        return identityFileEnabled;
    }

    public void setIdentityFileEnabled(boolean identityFileEnabled) {
        this.identityFileEnabled = identityFileEnabled;
    }
}
