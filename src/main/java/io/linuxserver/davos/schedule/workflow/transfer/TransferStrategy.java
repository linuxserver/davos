package io.linuxserver.davos.schedule.workflow.transfer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.workflow.actions.PostDownloadAction;
import io.linuxserver.davos.schedule.workflow.actions.PostDownloadExecution;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.connection.Connection;

public abstract class TransferStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferStrategy.class);

    protected Connection connection;
    private List<PostDownloadAction> postDownloadActions = new ArrayList<PostDownloadAction>();

    public TransferStrategy(Connection connection) {
        this.connection = connection;
    }

    public void setPostDownloadActions(List<PostDownloadAction> postDownloadActions) {
        this.postDownloadActions = postDownloadActions;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public abstract void transferFile(FTPFile fileToTransfer, String destination);

    protected void runPostDownloadAction(FTPFile file) {

        if (null == postDownloadActions) {

            LOGGER.warn("Post download actions have been nulled! This should not happen. Will not attempt run of actions");
            return;
        }

        LOGGER.debug("Running actions...");
        for (PostDownloadAction action : postDownloadActions) {

            PostDownloadExecution execution = new PostDownloadExecution();
            execution.fileName = file.getName();

            action.execute(execution);
        }
        LOGGER.debug("Finished running actions...");
    }
}
