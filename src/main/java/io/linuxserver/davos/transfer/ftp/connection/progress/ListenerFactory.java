package io.linuxserver.davos.transfer.ftp.connection.progress;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ListenerFactory {

    public ProgressListener createListener(TransferProtocol protocol) {
        
        if (TransferProtocol.SFTP.equals(protocol))
            return new SFTPProgressListener();
        
        return new ProgressListener();
    }
}
