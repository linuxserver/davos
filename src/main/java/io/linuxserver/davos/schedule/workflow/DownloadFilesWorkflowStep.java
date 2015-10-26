package io.linuxserver.davos.schedule.workflow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.ScheduleConfiguration;
import io.linuxserver.davos.schedule.workflow.transfer.TransferStrategy;
import io.linuxserver.davos.schedule.workflow.transfer.TransferStrategyFactory;
import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;

public class DownloadFilesWorkflowStep extends WorkflowStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFilesWorkflowStep.class);

    private List<FTPFile> filesToDownload = new ArrayList<FTPFile>();
    private TransferStrategyFactory transferStrategyFactory = new TransferStrategyFactory();

    public DownloadFilesWorkflowStep() {
        this.nextStep = new DisconnectWorkflowStep();
    }

    @Override
    public void runStep(ScheduleWorkflow schedule) {

        ScheduleConfiguration config = schedule.getConfig();

        TransferStrategy strategyToUse = transferStrategyFactory.getStrategy(config.getTransferType(), schedule.getConnection());
        LOGGER.debug("Strategy chosen for downloads is {}, selected {}", config.getTransferType(), strategyToUse);
        strategyToUse.setPostDownloadActions(schedule.getConfig().getActions());
        LOGGER.debug("PostDownloadActions: {} have been set against chosen strategy", schedule.getConfig().getActions());

        try {

            for (FTPFile file : filesToDownload)
                strategyToUse.transferFile(file, config.getLocalFilePath());

            LOGGER.info("Download step complete. Moving onto next step");

        } catch (FTPException e) {

            LOGGER.error("Unable to complete download. Error was: {}", e.getMessage());
            LOGGER.debug("Stacktrace", e);
            LOGGER.info("Will still continue to next step");
        }

        nextStep.runStep(schedule);
    }

    public void setFilesToDownload(List<FTPFile> filesToDownload) {
        this.filesToDownload = filesToDownload;
    }
}
