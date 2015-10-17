package io.linuxserver.davos.transfer.ftp.exception;

public abstract class FTPException extends RuntimeException {

    private static final long serialVersionUID = 7733358928451506618L;

    public FTPException() {
        super();
    }
    
    public FTPException(String message) {
        super(message);
    }
    
    public FTPException(String message, Exception cause) {
        super(message, cause);
    }
}
