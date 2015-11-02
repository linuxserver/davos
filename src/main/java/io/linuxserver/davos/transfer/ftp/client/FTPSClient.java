package io.linuxserver.davos.transfer.ftp.client;

public class FTPSClient extends FTPClient {

    public FTPSClient() {
        ftpClient = new org.apache.commons.net.ftp.FTPSClient("SSL", true);
    }
}
