package io.linuxserver.davos.transfer.ftp.connection;

import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class FTPConnection implements Connection {

    public FTPConnection(org.apache.commons.net.ftp.FTPClient client) {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public String currentDirectory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void download(FTPFile remoteFilePath, String localFilePath) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<FTPFile> listFiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<FTPFile> listFiles(String remoteDirectory) {
        // TODO Auto-generated method stub
        return null;
    }

}
