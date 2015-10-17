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
    public void shouldReturnNullForAnythingElse() {
        
        assertThat(new ClientFactory().getClient(TransferProtocol.FTP)).isNull();
        assertThat(new ClientFactory().getClient(TransferProtocol.FTPS)).isNull();
    }
}
