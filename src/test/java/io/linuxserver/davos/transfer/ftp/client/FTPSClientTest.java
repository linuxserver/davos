package io.linuxserver.davos.transfer.ftp.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FTPSClientTest {

    private FTPClient client = new FTPSClient();

    @Test
    public void newFtpsClientShouldCreateFTPSClientInstance() {
        assertThat(client.ftpClient).isInstanceOf(org.apache.commons.net.ftp.FTPSClient.class);
    }
}
