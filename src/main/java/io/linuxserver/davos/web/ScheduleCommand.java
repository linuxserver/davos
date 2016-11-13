package io.linuxserver.davos.web;

public class ScheduleCommand {

    public Command command;
    
    public enum Command {
        START, STOP
    }
}
