package io.linuxserver.davos.transfer.ftp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPSClient extends FTPClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPSClient.class);
    
    public FTPSClient() {
        
        LOGGER.debug("Initialising FTPS Client");
        ftpClient = new org.apache.commons.net.ftp.FTPSClient("SSL", true);
    }
}
