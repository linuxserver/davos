package io.linuxserver.davos.transfer.ftp.exception;

public class ClientDisconnectException extends FTPException {

    private static final long serialVersionUID = 7733358928451506618L;

    public ClientDisconnectException() {
        super();
    }
    
    public ClientDisconnectException(String message) {
        super(message);
    }
    
    public ClientDisconnectException(String message, Exception cause) {
        super(message, cause);
    }
}
