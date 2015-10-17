package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class FilesAndFoldersTranferStrategies extends TransferStrategy {

    public FilesAndFoldersTranferStrategies(Connection connection) {
        super(connection);
    }

    @Override
    public void transfer(FTPFile fileToTransfer, String destination) {
        // TODO Auto-generated method stub
    }
}
