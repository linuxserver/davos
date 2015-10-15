package io.linuxserver.davos.transfer.ftp.connection;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

public class ConnectionFactory {

    public SFTPConnection createSftpConnection(Channel channel) {
        return new SFTPConnection((ChannelSftp) channel);
    }
}
