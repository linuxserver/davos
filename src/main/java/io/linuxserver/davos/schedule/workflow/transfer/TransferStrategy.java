package io.linuxserver.davos.schedule.workflow.transfer;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public abstract class TransferStrategy {

    protected Connection connection;

    public TransferStrategy(Connection connection) {
        this.connection = connection;
    }

    public abstract void transferFile(FTPFile fileToTransfer, String destination);
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
