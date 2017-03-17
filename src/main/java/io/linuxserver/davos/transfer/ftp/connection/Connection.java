package io.linuxserver.davos.transfer.ftp.connection;

import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public interface Connection {
    
    String currentDirectory() throws FTPException;
    
    void download(FTPFile remoteFilePath, String localFilePath) throws FTPException;
    
    void deleteRemoteFile(FTPFile file) throws FTPException;
    
    List<FTPFile> listFiles() throws FTPException;
    
    List<FTPFile> listFiles(String remoteDirectory) throws FTPException;
    
    void setProgressListener(ProgressListener progressListener);
}
