package io.linuxserver.davos.schedule.workflow.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;
import io.linuxserver.davos.util.FileUtils;

public class FilesOnlyTransferStrategy extends TransferStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesOnlyTransferStrategy.class);
    
    public FilesOnlyTransferStrategy(Connection connection) {
        super(connection);
    }

    @Override
    public void transferFile(FTPTransfer transfer, String destination) {

        FTPFile file = transfer.getFile();
        String filename = file.getName();
        String cleanFilePath = FileUtils.ensureTrailingSlash(file.getPath());
        String cleanDestination = FileUtils.ensureTrailingSlash(destination);

        if (!file.isDirectory()) {
         
            LOGGER.info("Downloading {} to {}", cleanFilePath + filename, cleanDestination);
            transfer.setState(FTPTransfer.State.DOWNLOADING);
            connection.download(file, cleanDestination);
            transfer.setState(FTPTransfer.State.FINISHED);
            LOGGER.info("Successfully downloaded file.");
            
            LOGGER.info("Running post download actions on {}", filename);
            runPostDownloadAction(file);
        } else {
            
            LOGGER.debug("Nullifying listener as it will never get used");
            transfer.setState(FTPTransfer.State.SKIPPED);
            transfer.setListener(null);
        }
    }
}
