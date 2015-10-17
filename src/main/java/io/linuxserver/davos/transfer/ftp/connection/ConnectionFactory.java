package io.linuxserver.davos.transfer.ftp.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

public class ConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);
    
    public SFTPConnection createSFTPConnection(Channel channel) {
        LOGGER.debug("Creating SFTP connection for channel {}", channel);
        return new SFTPConnection((ChannelSftp) channel);
    }
    
    public FTPConnection createFTPConnection(org.apache.commons.net.ftp.FTPClient client) {
        LOGGER.debug("Creating FTP connection for client {}", client);
        return new FTPConnection(client);
    }
}
