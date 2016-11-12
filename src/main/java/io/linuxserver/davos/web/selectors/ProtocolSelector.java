package io.linuxserver.davos.web.selectors;

public enum ProtocolSelector {

    FTP, FTPS, SFTP;
    
    public static final ProtocolSelector[] ALL = { FTP, FTPS, SFTP };
}
