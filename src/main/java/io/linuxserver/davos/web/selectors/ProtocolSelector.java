package io.linuxserver.davos.web.selectors;

public enum ProtocolSelector {

    FTP("FTP"), FTPS("FTPS"), SFTP("SFTP");
    
    public static final ProtocolSelector[] ALL = { FTP, FTPS, SFTP };
    
    private final String text;
    
    private ProtocolSelector(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
}
