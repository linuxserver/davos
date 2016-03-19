package io.linuxserver.davos.transfer.ftp.connection.progress;

import com.jcraft.jsch.SftpProgressMonitor;

public class SFTPProgressListener extends ProgressListener implements SftpProgressMonitor {
  
    @Override
    public void init(int op, String src, String dest, long max) {
        setTotalBytes(max);
        start();
    }

    @Override
    public boolean count(long count) {
        setBytesWritten(count);
        return true;
    }

    @Override
    public void end() {
        reset();
    }
}
