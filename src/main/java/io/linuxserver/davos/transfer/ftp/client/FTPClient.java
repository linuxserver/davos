package io.linuxserver.davos.transfer.ftp.client;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPReply;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;
import io.linuxserver.davos.transfer.ftp.exception.ClientConnectionException;
import io.linuxserver.davos.transfer.ftp.exception.ClientDisconnectException;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public class FTPClient extends Client {

    private ConnectionFactory connectionFactory = new ConnectionFactory();

    protected org.apache.commons.net.ftp.FTPClient ftpClient;

    public FTPClient() {
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

            if (ftpClient.isConnected())
                ftpClient.disconnect();

        } catch (IOException e) {
            throw new ClientDisconnectException("There was an unexpected error while trying to disconnect.", e);
        }
    }

    private void connectClientAndCheckStatus() throws SocketException, IOException, FTPException {

        ftpClient.connect(host, port);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            
            ftpClient.disconnect();
            
            throw new ClientConnectionException(String.format("The host %s on port %d returned a bad status code.", host, port));
        }
    }

    private void login() throws IOException, FTPException {

        boolean hasLoggedIn = ftpClient.login(userCredentials.getUsername(), userCredentials.getPassword());

        if (!hasLoggedIn)
            throw new ClientConnectionException(String.format("Unable to login for user %s", userCredentials.getUsername()));

        ftpClient.setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
    }

    private void setSpecificModesOnClient() throws IOException {

        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlKeepAliveTimeout(300);
    }
}
