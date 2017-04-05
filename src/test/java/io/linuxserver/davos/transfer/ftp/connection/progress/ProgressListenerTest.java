package io.linuxserver.davos.transfer.ftp.connection.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ProgressListenerTest {

    @Test
    public void shouldGiveCorrectSpeed() throws InterruptedException {

        ProgressListener listener = new ProgressListener();

        listener.start();
        Thread.sleep(1000);
        listener.setBytesWritten(1000000);

        assertThat(listener.getTransferSpeed()).isBetween(0.9, 1.1);
    }

    @Test
    public void shouldGiveCorrectSpeedWhenAlternating() throws InterruptedException {

        ProgressListener listener = new ProgressListener();

        listener.start();
        Thread.sleep(1000);
        listener.setBytesWritten(1000000);
        assertThat(listener.getTransferSpeed()).isBetween(0.9, 1.1);

        Thread.sleep(1000);
        listener.setBytesWritten(1500000);
        assertThat(listener.getTransferSpeed()).isBetween(0.45, 0.51);
    }

    @Test
    public void shouldReturn100IfTotalSizeIsZero() {

        ProgressListener listener = new ProgressListener();

        listener.start();
        listener.setTotalBytes(0);
        listener.setBytesWritten(0);
        
        assertThat(listener.getProgress()).isEqualTo(100);
    }
    
    @Test
    public void shouldReturn0IfTotalBytesWrittenIsZero() {

        ProgressListener listener = new ProgressListener();

        listener.start();
        listener.setTotalBytes(110);
        listener.setBytesWritten(0);
        
        assertThat(listener.getProgress()).isEqualTo(0);
    }

    @Test
    public void shouldShowProgress() {

        ProgressListener listener = new ProgressListener();

        listener.setTotalBytes(2000);

        listener.setBytesWritten(500);
        assertThat(listener.getProgress()).isEqualTo(25);

        listener.setBytesWritten(1000);
        assertThat(listener.getProgress()).isEqualTo(50);

        listener.setBytesWritten(2000);
        assertThat(listener.getProgress()).isEqualTo(100);
    }
}
