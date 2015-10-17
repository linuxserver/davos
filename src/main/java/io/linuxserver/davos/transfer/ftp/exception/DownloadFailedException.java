package io.linuxserver.davos.transfer.ftp.exception;

public class DownloadFailedException extends FTPException {

    private static final long serialVersionUID = 7733358928451506618L;

    public DownloadFailedException() {
        super();
    }
    
    public DownloadFailedException(String message) {
        super(message);
    }
    
    public DownloadFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
