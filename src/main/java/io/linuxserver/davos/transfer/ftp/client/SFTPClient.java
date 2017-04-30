package io.linuxserver.davos.transfer.ftp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;
import io.linuxserver.davos.transfer.ftp.exception.ClientConnectionException;
import io.linuxserver.davos.transfer.ftp.exception.ClientDisconnectException;

public class SFTPClient extends Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPClient.class);

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
            throw new ClientConnectionException(String.format("Unable to connect to host %s on port %d", host, port), e);
        }

        return connectionFactory.createSFTPConnection(channel);
    }

    @Override
    public void disconnect() {

        if (null == channel || null == session)
            throw new ClientDisconnectException("The underlying connection was never initially made.");

        LOGGER.debug("Disconnecting from channel");
        channel.disconnect();
        LOGGER.debug("Disconnecting from session");
        session.disconnect();
    }

    private void configureSessionAndConnect() throws JSchException {

        LOGGER.debug("Configuring connection credentials and options on session");

        if (null != userCredentials.getIdentity()) {
            
            String identityFile = userCredentials.getIdentity().getIdentityFile();
            LOGGER.debug("SSH identity found ({}). Setting against session", identityFile);
            jsch.addIdentity(identityFile);
        }
        
        session = jsch.getSession(userCredentials.getUsername(), host, port);
        session.setConfig("StrictHostKeyChecking", "no");

        // I'm going to have to think of a nicer way of doing this...
        if (null == userCredentials.getIdentity())
            session.setPassword(userCredentials.getPassword());

        session.connect();

        LOGGER.debug("Connected to session");
    }

    private void openChannelFromSession() throws JSchException {

        LOGGER.debug("Opening SFTP channel from session");

        channel = session.openChannel("sftp");
        channel.connect();

        LOGGER.debug("Connected to channel");
    }
}
