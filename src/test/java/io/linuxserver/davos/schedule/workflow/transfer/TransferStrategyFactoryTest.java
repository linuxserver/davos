package io.linuxserver.davos.schedule.workflow.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.FileTransferType;

public class TransferStrategyFactoryTest {

    @Test
    public void shouldReturnCorrectStrategies() {

        assertThat(new TransferStrategyFactory().getStrategy(FileTransferType.FILE, null))
                .isInstanceOf(FilesOnlyTransferStrategy.class);

        assertThat(new TransferStrategyFactory().getStrategy(FileTransferType.RECURSIVE, null))
                .isInstanceOf(FilesAndFoldersTranferStrategy.class);
    }
}
