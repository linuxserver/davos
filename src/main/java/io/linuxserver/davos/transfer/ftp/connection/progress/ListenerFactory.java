package io.linuxserver.davos.transfer.ftp.connection.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ListenerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerFactory.class);
    
    public ProgressListener createListener(TransferProtocol protocol) {
        
        if (TransferProtocol.SFTP.equals(protocol)) {
            LOGGER.debug("Chosen listener is SFTPProgressListener, for {}", protocol);
            return new SFTPProgressListener();
        }
        
        LOGGER.debug("Chosen listener is ProgressListener, for {}", protocol);
        return new ProgressListener();
    }
}
