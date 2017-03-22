package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;

public class FTPTransfer {

    private State state = State.PENDING;
    private final FTPFile file;
    private ProgressListener listener;

    public FTPTransfer(FTPFile file) {
        this.file = file;
    }

    public FTPFile getFile() {
        return file;
    }

    public ProgressListener getListener() {
        return listener;
    }

    public void setListener(ProgressListener listener) {
        this.listener = listener;
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        PENDING, DOWNLOADING, SKIPPED, FINISHED
    }
}
