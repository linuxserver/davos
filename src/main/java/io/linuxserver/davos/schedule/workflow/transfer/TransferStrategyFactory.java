package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class TransferStrategyFactory {

    public TransferStrategy getStrategy(FileTransferType type, Connection connection) {

        if (FileTransferType.FILE.equals(type))
            return new FilesOnlyTransferStrategy(connection);

        return new FilesAndFoldersTranferStrategy(connection);
    }
}
