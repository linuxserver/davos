package io.linuxserver.davos.transfer.ftp.client;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;

public class SFTPClient extends Client {

    private JSch jsch;
    private ConnectionFactory connectionFactory;

    private Session session;
    private Channel channel;

    public SFTPClient() {
        
        this.jsch = new JSch();
        this.connectionFactory = new ConnectionFactory();
    }

    @Override
    public Connection connect() {

        session = null;
        channel = null;

        try {

            configureSessionAndConnect();
            openChannelFromSession();

        } catch (JSchException e) {
            throw new RuntimeException(String.format("Unable to connect to host %s on port %d", host, port), e);
        }

        return connectionFactory.createSftpConnection(channel);
    }

    @Override
    public void disconnect() {

        if (null == channel || null == session)
            throw new RuntimeException("The underlying connection was never initially made.");

        channel.disconnect();
        session.disconnect();
    }

    private void configureSessionAndConnect() throws JSchException {

        session = jsch.getSession(userCredentials.getUsername(), host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(userCredentials.getPassword());

        session.connect();
    }

    private void openChannelFromSession() throws JSchException {

        channel = session.openChannel("sftp");
        channel.connect();
    }
}
