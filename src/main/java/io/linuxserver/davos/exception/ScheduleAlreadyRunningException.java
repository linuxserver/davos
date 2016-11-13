package io.linuxserver.davos.exception;

public class ScheduleAlreadyRunningException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ScheduleAlreadyRunningException() {
        super("The schedule is already running");
    }
}
