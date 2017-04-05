package io.linuxserver.davos.transfer.ftp.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;
import io.linuxserver.davos.transfer.ftp.connection.progress.SFTPProgressListener;
import io.linuxserver.davos.transfer.ftp.exception.DeleteFileException;
import io.linuxserver.davos.transfer.ftp.exception.DownloadFailedException;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;
import io.linuxserver.davos.transfer.ftp.exception.FileListingException;
import io.linuxserver.davos.util.FileUtils;

public class SFTPConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPConnection.class);

    private ChannelSftp channel;
    private FileUtils fileUtils = new FileUtils();
    private SFTPProgressListener progressListener;

    public SFTPConnection(ChannelSftp channel) {
        this.channel = channel;
    }

    @Override
    public String currentDirectory() {

        try {
            return channel.pwd();
        } catch (SftpException e) {
            throw new FileListingException("Unable to print the working directory", e);
        }
    }

    @Override
    public void download(FTPFile file, String localFilePath) {

        String path = FileUtils.ensureTrailingSlash(file.getPath()) + file.getName();
        String cleanLocalPath = FileUtils.ensureTrailingSlash(localFilePath);

        try {

            if (file.isDirectory())
                downloadDirectoryAndContents(file, cleanLocalPath, path);
            else
                doGet(path, cleanLocalPath);

        } catch (SftpException e) {
            throw new DownloadFailedException("Unable to download file " + path, e);
        }
    }

    @Override
    public List<FTPFile> listFiles() {
        return listFiles(currentDirectory());
    }

    @Override
    public List<FTPFile> listFiles(String remoteDirectory) {

        try {

            String cleanRemoteDirectory = FileUtils.ensureTrailingSlash(remoteDirectory);

            List<FTPFile> files = new ArrayList<FTPFile>();

            LOGGER.debug("Listing files in {}", cleanRemoteDirectory);

            @SuppressWarnings("unchecked")
            Vector<LsEntry> lsEntries = channel.ls(cleanRemoteDirectory);

            for (LsEntry entry : lsEntries)
                files.add(toFtpFile(entry, cleanRemoteDirectory));

            LOGGER.debug("Listed {} items from remote directory {}", files.size(), cleanRemoteDirectory);

            return files;

        } catch (SftpException e) {
            throw new FileListingException(String.format("Unable to list files in directory %s", remoteDirectory), e);
        }
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = (SFTPProgressListener) progressListener;
    }

    private void doGet(String fullRemotePath, String fullLocalDownloadPath) throws SftpException {

        if (null != progressListener) {

            LOGGER.debug("Progress listener has been enabled");
            channel.get(fullRemotePath, fullLocalDownloadPath, progressListener);

        } else
            channel.get(fullRemotePath, fullLocalDownloadPath);
    }

    private void downloadDirectoryAndContents(FTPFile file, String localDownloadFolder, String path) throws SftpException {

        LOGGER.info("Item {} is a directory. Will now check sub-items", file.getName());
        List<FTPFile> subItems = listFiles(path).stream().filter(removeCurrentAndParentDirs()).collect(Collectors.toList());

        String fullLocalDownloadPath = FileUtils.ensureTrailingSlash(localDownloadFolder + file.getName());

        LOGGER.debug("Creating new local directory {}", fullLocalDownloadPath);
        fileUtils.createLocalDirectory(fullLocalDownloadPath);

        for (FTPFile subItem : subItems) {

            String subItemPath = FileUtils.ensureTrailingSlash(subItem.getPath()) + subItem.getName();

            if (subItem.isDirectory()) {

                String subLocalFilePath = FileUtils.ensureTrailingSlash(fullLocalDownloadPath);
                downloadDirectoryAndContents(subItem, subLocalFilePath, FileUtils.ensureTrailingSlash(subItemPath));
            }

            else {

                LOGGER.info("Downloading {} to {}", subItemPath, fullLocalDownloadPath);
                doGet(subItemPath, fullLocalDownloadPath);
            }
        }
    }

    private Predicate<? super FTPFile> removeCurrentAndParentDirs() {
        return file -> !file.getName().equals(".") && !file.getName().equals("..");
    }

    private FTPFile toFtpFile(LsEntry lsEntry, String filePath) throws SftpException {

        String name = lsEntry.getFilename();
        long fileSize = lsEntry.getAttrs().getSize();
        int mTime = lsEntry.getAttrs().getMTime();
        boolean directory = lsEntry.getAttrs().isDir();

        return new FTPFile(name, fileSize, filePath, (long) mTime * 1000, directory);
    }

    @Override
    public void deleteRemoteFile(FTPFile file) throws FTPException {

        LOGGER.debug("Deleting remote file...");
        String cleanRemotePath = FileUtils.ensureTrailingSlash(file.getPath()) + file.getName();

        try {

            if (file.isDirectory()) {
                deleteDirectoryAndContents(file, cleanRemotePath);
            } else
                doDelete(cleanRemotePath);

        } catch (SftpException e) {

            LOGGER.debug("channel threw exception. Assuming file not deleted");
            throw new DeleteFileException("Unable to delete file on remote server", e);
        }
    }
    
    private void deleteDirectoryAndContents(FTPFile file, String remoteDirectoryPath) throws SftpException {

        LOGGER.info("Item {} is a directory. Will now check sub-items", file.getName());
        List<FTPFile> subItems = listFiles(remoteDirectoryPath).stream().filter(removeCurrentAndParentDirs())
                .collect(Collectors.toList());
        
        for (FTPFile subItem : subItems) {
            
            String subItemPath = FileUtils.ensureTrailingSlash(subItem.getPath()) + subItem.getName();
            
            if (subItem.isDirectory())
                deleteDirectoryAndContents(subItem, subItemPath);
            else
                doDelete(subItemPath);
        }
        
        LOGGER.debug("Removing empty directory {}", remoteDirectoryPath);
        channel.rmdir(remoteDirectoryPath);
    }

    private void doDelete(String subItemPath) throws SftpException {
        
        LOGGER.debug("Deleting file: {}", subItemPath);
        channel.rm(subItemPath);
    }
}
