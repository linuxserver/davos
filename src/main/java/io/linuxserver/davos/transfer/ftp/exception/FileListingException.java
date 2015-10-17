package io.linuxserver.davos.transfer.ftp.exception;

public class FileListingException extends FTPException {

    private static final long serialVersionUID = 7733358928451506618L;

    public FileListingException() {
        super();
    }
    
    public FileListingException(String message) {
        super(message);
    }
    
    public FileListingException(String message, Exception cause) {
        super(message, cause);
    }
}
