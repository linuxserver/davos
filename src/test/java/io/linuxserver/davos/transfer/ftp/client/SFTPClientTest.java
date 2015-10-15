package io.linuxserver.davos.transfer.ftp.client;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.transfer.ftp.connection.ConnectionFactory;
import io.linuxserver.davos.transfer.ftp.connection.SFTPConnection;

public class SFTPClientTest {

    private static final String SFTP = "sftp";
    private static final String CONNECTION_ERROR_MESSAGE = "Unable to connect to host %s on port %d";

    @InjectMocks
    private SFTPClient SFTPClient = new SFTPClient();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JSch mockJsch;

    @Mock
    private ConnectionFactory mockConnectionFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserCredentials userCredentials;

    @Before
    public void setUp() throws JSchException {

        initMocks(this);

        userCredentials = new UserCredentials("user", "password");

        SFTPClient.setHost("host");
        SFTPClient.setPort(999);
        SFTPClient.setCredentials(userCredentials);

        when(mockConnectionFactory.createSftpConnection(any(Channel.class))).thenReturn(new SFTPConnection(new ChannelSftp()));
    }

    @Test
    public void connectMethodShouldCreateSessionUsingHostPortAndUsername() throws JSchException {

        SFTPClient.connect();

        verify(mockJsch).getSession("user", "host", 999);
    }

    @Test
    public void sessionFromInitialConnectionNeedsConfigAndPasswordSettingBeforeConnecting() throws JSchException {

        Session mockSession = mockJsch.getSession("user", "host", 999);

        InOrder inOrder = Mockito.inOrder(mockSession);

        SFTPClient.connect();

        inOrder.verify(mockSession).setConfig("StrictHostKeyChecking", "no");
        inOrder.verify(mockSession).setPassword("password");
        inOrder.verify(mockSession).connect();
    }

    @Test
    public void returnedSessionObjectShouldSetChannelToSftpAndOpen() throws JSchException {

        Session mockSession = mockJsch.getSession("user", "host", 999);

        SFTPClient.connect();

        verify(mockSession).openChannel(SFTP);
    }

    @Test
    public void ifForAnyReasonTheUnderlyingSessionCantConnectThenCatchTheExceptionAndRethrow() throws JSchException {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(is(equalTo(String.format(CONNECTION_ERROR_MESSAGE, "host", 999))));

        Session mockSession = mockJsch.getSession("user", "host", 999);
        doThrow(new JSchException()).when(mockSession).connect();

        SFTPClient.connect();
    }

    @Test
    public void sessionChannelShouldBeConnectedTo() throws JSchException {

        Session mockSession = mockJsch.getSession("user", "host", 999);
        Channel mockChannel = mockSession.openChannel(SFTP);

        SFTPClient.connect();

        verify(mockChannel).connect();
    }

    @Test
    public void connectMethodShouldReturnLiveInstanceOfSftpChannelWrappedInStfpConnection() {

        Connection connection = SFTPClient.connect();

        assertThat(connection).isInstanceOf(SFTPConnection.class);
    }

    @Test
    public void disconnectMethodShouldDisconnectUnderlyingChannelAndSession() throws JSchException {

        Session mockSession = mockJsch.getSession("user", "host", 999);
        Channel mockChannel = mockSession.openChannel(SFTP);

        SFTPClient.connect();
        SFTPClient.disconnect();

        verify(mockSession).disconnect();
        verify(mockChannel).disconnect();
    }

    @Test
    public void disconnectMethodShouldThrowExceptionWhenNotInitiallyConnected() {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(is(equalTo("The underlying connection was never initially made.")));

        SFTPClient.disconnect();
    }
}
