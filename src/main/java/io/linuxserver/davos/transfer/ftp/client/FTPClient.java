package io.linuxserver.davos.transfer.ftp.client;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;
import io.linuxserver.davos.transfer.ftp.exception.ClientConnectionException;
import io.linuxserver.davos.transfer.ftp.exception.ClientDisconnectException;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public class FTPClient extends Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPClient.class);
    
    private ConnectionFactory connectionFactory = new ConnectionFactory();

    protected org.apache.commons.net.ftp.FTPClient ftpClient;

    public FTPClient() {
        
        LOGGER.debug("Initialising FTP Client");
        ftpClient = new org.apache.commons.net.ftp.FTPClient();
    }

    public Connection connect() {

        try {

            connectClientAndCheckStatus();
            setSpecificModesOnClient();
            login();

        } catch (IOException e) {
            throw new ClientConnectionException(String.format("Unable to connect to host %s on port %d", host, port), e);
        }

        return connectionFactory.createFTPConnection(ftpClient);
    }

    public void disconnect() {

        try {

            if (null == ftpClient)
                throw new ClientDisconnectException("The underlying client was null.");

            if (ftpClient.isConnected()) {
                LOGGER.debug("Disconnecting...");
                ftpClient.disconnect();
                LOGGER.debug("Disconnected");
            }

        } catch (IOException e) {
            throw new ClientDisconnectException("There was an unexpected error while trying to disconnect.", e);
        }
    }

    private void connectClientAndCheckStatus() throws SocketException, IOException, FTPException {

        LOGGER.debug("Connecting to {}:{}", host, port);
        ftpClient.connect(host, port);

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            
            LOGGER.debug("Connection not made.");
            LOGGER.debug("Response status: {}", replyCode);
            LOGGER.debug("Disconnecting");
            ftpClient.disconnect();
            LOGGER.debug("Disconnected");
            
            throw new ClientConnectionException(String.format("The host %s on port %d returned a bad status code.", host, port));
        }
    }

    private void login() throws IOException, FTPException {

        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        LOGGER.debug("Username: {}", username);
        boolean hasLoggedIn = ftpClient.login(username, password);

        if (!hasLoggedIn)
            throw new ClientConnectionException(String.format("Unable to login for user %s", username));

        ftpClient.setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
    }

    private void setSpecificModesOnClient() throws IOException {

        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlKeepAliveTimeout(300);
    }
}
