package io.linuxserver.davos.transfer.ftp.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;
import io.linuxserver.davos.transfer.ftp.connection.FTPConnection;
import io.linuxserver.davos.transfer.ftp.exception.ClientConnectionException;
import io.linuxserver.davos.transfer.ftp.exception.ClientDisconnectException;

public class FTPClientTest {

    @InjectMocks
    public FTPClient ftpClient = new FTPClient();

    @Mock
    private org.apache.commons.net.ftp.FTPClient mockFtpClient;

    @Mock
    private ConnectionFactory mockConnectionFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private String hostname;
    private int port;
    private UserCredentials userCredentials;

    @Before
    public void setUp() throws IOException {
        
        initMocks(this);

        hostname = "this is a hostname";
        port = 80;

        userCredentials = new UserCredentials("thisisausername", "thisisapassword");

        ftpClient.setHost(hostname);
        ftpClient.setPort(port);
        ftpClient.setCredentials(userCredentials);

        when(mockFtpClient.getReplyCode()).thenReturn(200);
        when(mockFtpClient.login(userCredentials.getUsername(), userCredentials.getPassword())).thenReturn(true);
        when(mockFtpClient.isConnected()).thenReturn(true);

        when(mockConnectionFactory.createFTPConnection(mockFtpClient)).thenReturn(new FTPConnection(mockFtpClient));
    }

    @Test
    public void newFtpClientShouldCreateFTPClientInstance() {
        assertThat(ftpClient.ftpClient).isInstanceOf(org.apache.commons.net.ftp.FTPClient.class);
    }

    @Test
    public void connectMethodShouldCallonUnderlyingFtpClientConnectMethodWithHostname() throws SocketException, IOException {

        ftpClient.connect();

        verify(mockFtpClient).connect(hostname, port);
    }

    @Test
    public void connectMethodShouldEnterPassiveModeLoginToUnderlyingFtpClient() throws IOException {

        ftpClient.connect();

        InOrder inOrder = Mockito.inOrder(mockFtpClient);

        inOrder.verify(mockFtpClient).enterLocalPassiveMode();
        inOrder.verify(mockFtpClient).login(userCredentials.getUsername(), userCredentials.getPassword());
    }

    @Test
    public void connectMethodShouldSetKeepAliveCommandToEveryFiveMinutes() {

        ftpClient.connect();

        verify(mockFtpClient).setControlKeepAliveTimeout(300);
    }

    @Test
    public void onceLoggedInTheClientShouldHaveFileTypeSetToBinary() throws IOException {
        
        ftpClient.connect();

        InOrder inOrder = Mockito.inOrder(mockFtpClient);
        
        inOrder.verify(mockFtpClient).login(userCredentials.getUsername(), userCredentials.getPassword());
        inOrder.verify(mockFtpClient).setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
    }

    @Test
    public void connectMethodShouldReturnNewFtpConnectionTakingInUnderlyingFtpClient() {

        Connection connection = ftpClient.connect();

        verify(mockConnectionFactory).createFTPConnection(mockFtpClient);
        assertThat(connection).isInstanceOf(FTPConnection.class);
    }

    @Test
    public void disconnectMethodShouldCallOnUnderlyingFtpClientDisconnectMethod() throws IOException {

        ftpClient.disconnect();

        verify(mockFtpClient).disconnect();
    }

    @Test
    public void ifConnectionFailsThenCatchThrownExceptionAndThrowFtpException() throws SocketException, IOException {

        expectedException.expect(ClientConnectionException.class);
        expectedException.expectMessage(is(equalTo("Unable to connect to host " + hostname + " on port " + port)));

        doThrow(new IOException()).when(mockFtpClient).connect(hostname, port);

        ftpClient.connect();
    }

    @Test
    public void ifConnectionFailsDueToUnknownHostThenCatchThrownExceptionAndThrowFtpException() throws SocketException,
            IOException {

        expectedException.expect(ClientConnectionException.class);
        expectedException.expectMessage(is(equalTo("Unable to connect to host " + hostname + " on port " + port)));

        doThrow(new UnknownHostException()).when(mockFtpClient).connect(hostname, port);

        ftpClient.connect();
    }

    @Test
    public void ifUnderlyingClientReturnsBadConnectionCodeThenThrowConnectionException() {

        expectedException.expect(ClientConnectionException.class);
        expectedException
                .expectMessage(is(equalTo("The host " + hostname + " on port " + port + " returned a bad status code.")));

        when(mockFtpClient.getReplyCode()).thenReturn(500);

        ftpClient.connect();
    }

    @Test
    public void ifUnableToLoginToFtpClientThenThrowFtpException() throws IOException {

        expectedException.expect(ClientConnectionException.class);
        expectedException.expectMessage(is(equalTo("Unable to login for user " + userCredentials.getUsername())));

        when(mockFtpClient.login(userCredentials.getUsername(), userCredentials.getPassword())).thenReturn(false);

        ftpClient.connect();
    }

    @Test
    public void whenDisconnectingThenClientShouldCheckToSeeIfAlreadyDisconnected() {

        ftpClient.disconnect();

        verify(mockFtpClient).isConnected();
    }

    @Test
    public void whenAlreadyDisconnectedThenClientShoudlNotCallOnUnderlyingClientDisconnectMethod() throws IOException {

        when(mockFtpClient.isConnected()).thenReturn(false);

        ftpClient.disconnect();

        verify(mockFtpClient, times(0)).disconnect();
    }

    @Test
    public void whenClientIsStillConnectedThenShouldCallOnUnderlyingClientDisconnectMethod() throws IOException {

        ftpClient.disconnect();

        verify(mockFtpClient).disconnect();
    }

    @Test
    public void ifUnderlyingClientThrowsExceptionWhenDisconnectingThenClientShouldCatchAndRethrow() throws IOException {

        expectedException.expect(ClientDisconnectException.class);
        expectedException.expectMessage(is(equalTo("There was an unexpected error while trying to disconnect.")));

        doThrow(new IOException()).when(mockFtpClient).disconnect();

        ftpClient.disconnect();
    }
    
    @Test
    public void ifUnderlyingClientIsNullifiedBeforeDisconnectionThenDisconnectShouldThrow() {
        
        expectedException.expect(ClientDisconnectException.class);
        expectedException.expectMessage(is(equalTo("The underlying client was null.")));
        
        ftpClient.ftpClient = null;
        ftpClient.disconnect();
    }
}
