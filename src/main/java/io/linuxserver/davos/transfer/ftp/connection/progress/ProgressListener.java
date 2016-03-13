package io.linuxserver.davos.transfer.ftp.connection.progress;

public interface ProgressListener {

    double getProgress();
    void reset();
}
