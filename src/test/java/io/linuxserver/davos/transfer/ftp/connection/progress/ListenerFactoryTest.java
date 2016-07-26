package io.linuxserver.davos.transfer.ftp.connection.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ListenerFactoryTest {

    @Test
    public void shouldReturnCorrectListener() {
        
        ListenerFactory listenerFactory = new ListenerFactory();
        
        assertThat(listenerFactory.createListener(TransferProtocol.FTP)).isInstanceOf(ProgressListener.class);
        assertThat(listenerFactory.createListener(TransferProtocol.FTPS)).isInstanceOf(ProgressListener.class);
        assertThat(listenerFactory.createListener(TransferProtocol.SFTP)).isInstanceOf(SFTPProgressListener.class);
    }
}
