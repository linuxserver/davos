package io.linuxserver.davos.transfer.ftp.exception;

public class DeleteFileException extends FTPException {

    private static final long serialVersionUID = 6191478212036531333L;

    public DeleteFileException() {
        super();
    }
    
    public DeleteFileException(String message) {
        super(message);
    }
    
    public DeleteFileException(String message, Exception cause) {
        super(message, cause);
    } 
}
