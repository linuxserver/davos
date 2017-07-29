package io.linuxserver.davos.exception;

public class HostInUseException extends RuntimeException {

    private static final long serialVersionUID = 618892455818185964L;

    public HostInUseException(String message) {
        super(message);
    }
}
