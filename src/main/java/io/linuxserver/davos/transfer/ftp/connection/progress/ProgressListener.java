package io.linuxserver.davos.transfer.ftp.connection.progress;

public class ProgressListener {

    private long lastWriteTime;
    private long totalBytesWritten;
    private long bytesInWrite;
    private long totalBytes;
    private double currentTransferSpeed;

    public double getProgress() {
        
        if (totalBytes > 0)
            return ((double) totalBytesWritten / (double) totalBytes) * 100;

        return 100;
    }

    public double getTransferSpeed() {
        return currentTransferSpeed;
    }

    public void reset() {
        totalBytes = 0;
    }

    public void updateBytesWritten(long bytes) {
        setBytesWritten(totalBytesWritten + bytes);
    }

    public void setBytesWritten(long bytesWritten) {

        long currentTimeMillis = System.currentTimeMillis();
        long timeSinceLastWrite = currentTimeMillis - this.lastWriteTime;

        this.lastWriteTime = currentTimeMillis;
        this.bytesInWrite = bytesWritten - this.totalBytesWritten;
        this.totalBytesWritten = bytesWritten;

        this.currentTransferSpeed = (double) this.bytesInWrite / (double) timeSinceLastWrite / 1000;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public void start() {

        lastWriteTime = System.currentTimeMillis();
        totalBytesWritten = 0;
    }
}
