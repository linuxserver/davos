package io.linuxserver.davos.transfer.ftp.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.output.CountingOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.progress.FTPProgressListener;
import io.linuxserver.davos.transfer.ftp.connection.progress.ProgressListener;
import io.linuxserver.davos.transfer.ftp.exception.DownloadFailedException;
import io.linuxserver.davos.transfer.ftp.exception.FileListingException;
import io.linuxserver.davos.util.FileStreamFactory;
import io.linuxserver.davos.util.FileUtils;

public class FTPConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPConnection.class);

    private org.apache.commons.net.ftp.FTPClient client;
    private FileStreamFactory fileStreamFactory = new FileStreamFactory();
    private FileUtils fileUtils = new FileUtils();
    private FTPProgressListener progressListener;

    public FTPConnection(org.apache.commons.net.ftp.FTPClient client) {
        this.client = client;
    }

    @Override
    public String currentDirectory() {

        try {
            return client.printWorkingDirectory();
        } catch (IOException e) {
            throw new FileListingException("Unable to print the working directory", e);
        }
    }

    @Override
    public void download(FTPFile file, String localFilePath) {

        String cleanRemotePath = FileUtils.ensureTrailingSlash(file.getPath()) + file.getName();
        String cleanLocalPath = FileUtils.ensureTrailingSlash(localFilePath);

        try {

            if (file.isDirectory())
                downloadDirectoryAndContents(file, cleanLocalPath, cleanRemotePath);

            else
                doDownload(file, cleanRemotePath, cleanLocalPath);

        } catch (FileNotFoundException e) {
            throw new DownloadFailedException(
                    String.format("Unable to write to local directory %s", cleanLocalPath + file.getName()), e);
        } catch (IOException e) {
            throw new DownloadFailedException(String.format("Unable to download file %s", cleanRemotePath), e);
        }
    }

    @Override
    public List<FTPFile> listFiles() {
        return listFiles(currentDirectory());
    }

    @Override
    public List<FTPFile> listFiles(String remoteDirectory) {

        List<FTPFile> files = new ArrayList<FTPFile>();

        try {

            String cleanRemoteDirectory = FileUtils.ensureTrailingSlash(remoteDirectory);
            LOGGER.debug("Listing all files in {}", cleanRemoteDirectory);
            org.apache.commons.net.ftp.FTPFile[] ftpFiles = client.listFiles(cleanRemoteDirectory);

            for (org.apache.commons.net.ftp.FTPFile file : ftpFiles)
                files.add(toFtpFile(file, cleanRemoteDirectory));

        } catch (IOException e) {
            throw new FileListingException(String.format("Unable to list files in directory %s", remoteDirectory), e);
        }

        return files.stream().filter(removeCurrentAndParentDirs()).collect(Collectors.toList());
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = (FTPProgressListener) progressListener;
    }

    private CountingOutputStream listenOn(OutputStream outputStream) {

        LOGGER.debug("Creating wrapping output stream for progress listener");
        
        CountingOutputStream countingStream = new CountingOutputStream(outputStream) {

            @Override
            protected void beforeWrite(int n) {

                super.beforeWrite(n);
                progressListener.updateBytesWritten(getByteCount());
            }
        };

        return countingStream;
    }

    private void doDownload(FTPFile file, String cleanRemotePath, String cleanLocalPath)
            throws FileNotFoundException, IOException {

        LOGGER.info("Downloading {} to {}", cleanRemotePath, cleanLocalPath);
        LOGGER.debug("Creating output stream for file {}", cleanLocalPath + file.getName());

        OutputStream outputStream = fileStreamFactory.createOutputStream(cleanLocalPath + file.getName());

        boolean hasDownloaded;

        if (null != progressListener) {
         
            LOGGER.debug("ProgressListener has been set. Initialising...");
            LOGGER.debug("Total file size is {}", file.getSize());
            progressListener.reset();
            progressListener.setTotalSize(file.getSize());
            
            hasDownloaded = client.retrieveFile(cleanRemotePath, listenOn(outputStream));
        }
        else
            hasDownloaded = client.retrieveFile(cleanRemotePath, outputStream);

        outputStream.close();

        if (!hasDownloaded)
            throw new DownloadFailedException("Server returned failure while downloading.");
    }

    private void downloadDirectoryAndContents(FTPFile file, String localDownloadFolder, String path) throws IOException {

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

            else
                doDownload(subItem, subItemPath, fullLocalDownloadPath);
        }
    }

    private Predicate<? super FTPFile> removeCurrentAndParentDirs() {
        return file -> !file.getName().equals(".") && !file.getName().equals("..");
    }

    private FTPFile toFtpFile(org.apache.commons.net.ftp.FTPFile ftpFile, String filePath) throws IOException {

        String name = ftpFile.getName();
        long fileSize = ftpFile.getSize();
        long mTime = ftpFile.getTimestamp().getTime().getTime();
        boolean isDirectory = ftpFile.isDirectory();

        return new FTPFile(name, fileSize, filePath, mTime, isDirectory);
    }
}
