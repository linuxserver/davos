package io.linuxserver.davos.dto;

import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class HostDTO {

    public String name;
    public String address;
    public int port;
    public String username;
    public String password;
    public TransferProtocol protocol = TransferProtocol.FTP;
}
