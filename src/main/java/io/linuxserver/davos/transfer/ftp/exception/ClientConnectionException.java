package io.linuxserver.davos.transfer.ftp.exception;

public class ClientConnectionException extends FTPException {

    private static final long serialVersionUID = 7733358928451506618L;

    public ClientConnectionException() {
        super();
    }
    
    public ClientConnectionException(String message) {
        super(message);
    }
    
    public ClientConnectionException(String message, Exception cause) {
        super(message, cause);
    }
}
