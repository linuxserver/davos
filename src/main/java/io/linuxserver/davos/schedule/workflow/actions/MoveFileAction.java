package io.linuxserver.davos.schedule.workflow.actions;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.util.FileUtils;

public class MoveFileAction implements PostDownloadAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveFileAction.class);

    private String currentFilePath;
    private String newFilePath;

    private FileUtils fileUtils = new FileUtils();

    public MoveFileAction(String currentFilePath, String newFilePath) {
        
        this.currentFilePath = FileUtils.ensureTrailingSlash(currentFilePath);
        this.newFilePath = FileUtils.ensureTrailingSlash(newFilePath);
    }

    @Override
    public void execute(PostDownloadExecution execution) {

        try {

            LOGGER.info("Executing move action: Moving {} to {}", execution.fileName, newFilePath);
            fileUtils.moveFileToDirectory(currentFilePath + execution.fileName, newFilePath);
            LOGGER.info("File successfully moved!");

        } catch (IOException e) {

            LOGGER.error("Unable to move {} to {}. Reason given: {}", execution.fileName, newFilePath, e.getMessage());
            LOGGER.debug("Full stack trace on error", e);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
