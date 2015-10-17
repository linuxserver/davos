package io.linuxserver.davos.schedule.workflow.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.linuxserver.davos.transfer.ftp.FileTransferType;

public class TransferStrategyFactoryTest {

    @Test
    public void shouldReturnCorrectStrategies() {

        assertThat(new TransferStrategyFactory().getStrategy(FileTransferType.FILES_ONLY, null))
                .isInstanceOf(FilesOnlyTransferStrategy.class);

        assertThat(new TransferStrategyFactory().getStrategy(FileTransferType.INCLUDE_FOLDERS, null))
                .isInstanceOf(FilesAndFoldersTranferStrategy.class);
    }
}
