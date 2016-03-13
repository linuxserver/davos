package io.linuxserver.davos.transfer.ftp.connection.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.connection.progress.SFTPProgressListener;

public class SFTPProgressListenerTest {

    @Test
    public void shouldReturnCorrectProgress() {
        
        SFTPProgressListener listener = new SFTPProgressListener();
        
        listener.init(0, "", "", 500);

        listener.count(100);
        assertThat(listener.getProgress()).isEqualTo(20);
        
        listener.count(250);
        assertThat(listener.getProgress()).isEqualTo(50);
        
        listener.count(500);
        assertThat(listener.getProgress()).isEqualTo(100);
    }
}
