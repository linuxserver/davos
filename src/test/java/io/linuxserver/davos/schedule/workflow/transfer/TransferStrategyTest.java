package io.linuxserver.davos.schedule.workflow.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class TransferStrategyTest {

    @Test
    public void toStringShouldPrintClassName() {
        assertThat(new TestTransferStrategy(null).toString()).isEqualTo("TestTransferStrategy");
        assertThat(new AnotherTestTransferStrategy(null).toString()).isEqualTo("AnotherTestTransferStrategy");
    }
    
    
    class TestTransferStrategy extends TransferStrategy {

        public TestTransferStrategy(Connection connection) {
            super(connection);
        }

        @Override
        public void transferFile(FTPFile fileToTransfer, String destination) {
        }
    }
    
    class AnotherTestTransferStrategy extends TransferStrategy {

        public AnotherTestTransferStrategy(Connection connection) {
            super(connection);
        }

        @Override
        public void transferFile(FTPFile fileToTransfer, String destination) {
        }
    }
}
