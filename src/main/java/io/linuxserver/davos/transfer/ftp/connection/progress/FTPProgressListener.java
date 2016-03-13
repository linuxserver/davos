package io.linuxserver.davos.transfer.ftp.connection.progress;

public class FTPProgressListener implements ProgressListener {

    private long totalSize;
    private long byteCount;

    @Override
    public double getProgress() {
        return ((double) byteCount / (double) totalSize) * 100;
    }
    
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
    
    public void updateBytesWritten(long byteCount) {
        this.byteCount = byteCount;
    }

    @Override
    public void reset() {
        totalSize = 0;
        byteCount = 0;
    }
}
