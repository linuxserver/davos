package io.linuxserver.davos.transfer.ftp.client;

import io.linuxserver.davos.transfer.ftp.connection.Connection;

public abstract class Client {

    protected String host;
    protected int port;
    
    protected UserCredentials userCredentials = UserCredentials.ANONYMOUS;
    
    public void setCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public abstract Connection connect();
    
    public abstract void disconnect();
}
