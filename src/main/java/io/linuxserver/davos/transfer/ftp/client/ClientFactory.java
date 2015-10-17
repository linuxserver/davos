package io.linuxserver.davos.transfer.ftp.client;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ClientFactory {

    public Client getClient(TransferProtocol protocol) {
        
        if (protocol.equals(TransferProtocol.SFTP))
            return new SFTPClient();
        
        return null;
    }
}
