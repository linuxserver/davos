package io.linuxserver.davos.transfer.ftp;

import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;

public class ActiveTransfer {

    public ProgressListener listener;
    public String source;
    public String target;
}
