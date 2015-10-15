package io.linuxserver.davos.transfer.ftp.connection;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelSftp;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class SFTPConnection implements Connection {

    private ChannelSftp channel;
    
    public SFTPConnection(ChannelSftp channel) {
        this.channel = channel;
    }

    @Override
    public String currentDirectory() {
        return null;
    }

    @Override
    public void download(String remoteFilePath, String localFilePath) {
    }

    @Override
    public List<FTPFile> listFiles() {
        return new ArrayList<FTPFile>();
    }

    @Override
    public List<FTPFile> listFiles(String remoteDirectory) {
        return new ArrayList<FTPFile>();
    }
}
