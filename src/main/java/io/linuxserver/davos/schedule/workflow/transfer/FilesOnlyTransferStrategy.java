package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class FilesOnlyTransferStrategy extends TransferStrategy {

    public FilesOnlyTransferStrategy(Connection connection) {
        super(connection);
    }

    @Override
    public void transfer(String from, String to) {
        // TODO Auto-generated method stub
    }
}
