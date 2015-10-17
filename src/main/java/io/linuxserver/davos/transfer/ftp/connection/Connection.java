package io.linuxserver.davos.transfer.ftp.connection;

import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public interface Connection {
    
    String currentDirectory() throws FTPException;
    
    void download(String remoteFilePath, String localFilePath) throws FTPException;
    
    List<FTPFile> listFiles() throws FTPException;
    
    List<FTPFile> listFiles(String remoteDirectory) throws FTPException;
}
