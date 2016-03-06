package io.linuxserver.davos.transfer.ftp.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ClientFactoryTest {

    @Test
    public void shouldReturnSFTPClientWhenProtocolIsSFTP() {
        assertThat(new ClientFactory().getClient(TransferProtocol.SFTP)).isInstanceOf(SFTPClient.class);
    }
    
    @Test
    public void shouldReturnFTPClientForAnythingElse() {
        
        assertThat(new ClientFactory().getClient(TransferProtocol.FTP)).isInstanceOf(FTPClient.class);
        assertThat(new ClientFactory().getClient(TransferProtocol.FTPS)).isInstanceOf(FTPSClient.class);
    }
}
