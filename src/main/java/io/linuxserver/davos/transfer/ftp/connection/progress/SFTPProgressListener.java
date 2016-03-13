package io.linuxserver.davos.transfer.ftp.connection.progress;

import com.jcraft.jsch.SftpProgressMonitor;

public class SFTPProgressListener implements SftpProgressMonitor, ProgressListener {

    private double progress;
    private long totalSize;

    @Override
    public double getProgress() {
        return (progress / (double) totalSize) * 100;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        totalSize = max;
    }

    @Override
    public boolean count(long count) {
        progress = count;
        return true;
    }

    @Override
    public void end() {
        reset();
    }

    @Override
    public void reset() {
        progress = 0;
        totalSize = 0;
    }
}
