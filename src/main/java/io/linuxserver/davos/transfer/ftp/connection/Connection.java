package io.linuxserver.davos.transfer.ftp.connection;

import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public interface Connection {
    
    String currentDirectory();
    
    void download(String remoteFilePath, String localFilePath);
    
    List<FTPFile> listFiles();
    
    List<FTPFile> listFiles(String remoteDirectory);
}
