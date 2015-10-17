package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.connection.Connection;

public class FileRecursiveTransferStrategy extends TransferStrategy {

    public FileRecursiveTransferStrategy(Connection connection) {
        super(connection);
    }

    @Override
    public void transfer(String from, String to) {
        // TODO Auto-generated method stub
    }
}
