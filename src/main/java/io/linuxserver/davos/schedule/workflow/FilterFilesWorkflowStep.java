package io.linuxserver.davos.schedule.workflow;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.FTPFile;
import io.linuxserver.davos.transfer.ftp.exception.FTPException;
import io.linuxserver.davos.util.PatternBuilder;

public class FilterFilesWorkflowStep extends WorkflowStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterFilesWorkflowStep.class);

    public FilterFilesWorkflowStep() {
        
        this.nextStep = new DownloadFilesWorkflowStep();
        this.backoutStep = new DisconnectWorkflowStep();
    }

    @Override
    public void runStep(ScheduleWorkflow schedule) {

        try {

            DateTime lastRun = schedule.getConfig().getLastRun();
            List<String> filters = schedule.getConfig().getFilters();

            List<FTPFile> allFiles = schedule.getConnection().listFiles(schedule.getConfig().getRemoteFilePath());
            List<FTPFile> filesToFilter = allFiles.stream().filter(after(lastRun)).collect(toList());
            List<FTPFile> filteredFiles = new ArrayList<FTPFile>();

            if (filters.isEmpty()) {

                LOGGER.info("Filter list was empty. Adding all found files to list");
                ((DownloadFilesWorkflowStep) nextStep).setFilesToDownload(filesToFilter);
                
            } else {

                for (FTPFile file : filesToFilter)
                    filterFilesByName(filters, filteredFiles, file);

                ((DownloadFilesWorkflowStep) nextStep).setFilesToDownload(filteredFiles);
            }

            LOGGER.info("Updating lastRun to {}", DateTime.now());
            schedule.getConfigurationDAO().updateLastRun(schedule.getConfig().getId(), DateTime.now());
            
            LOGGER.info("Successfully filtered files. Moving onto next step");
            nextStep.runStep(schedule);

        } catch (FTPException e) {

            LOGGER.error("Unable to filter files. Error message was: {}", e.getMessage());
            LOGGER.debug("Stacktrace", e);
            
            LOGGER.info("Backing out of this run.");
            backoutStep.runStep(schedule);
        }
    }

    private Predicate<? super FTPFile> after(DateTime lastRun) {
        
        LOGGER.debug("Filtering initial set of files by lastRun. Last run was {}", lastRun);
        return f -> f.getLastModified().isAfter(lastRun);
    }

    private void filterFilesByName(List<String> filters, List<FTPFile> filteredFiles, FTPFile file) {

        for (String filter : filters) {

            String expression = PatternBuilder.buildFromFilterString(filter);

            if (file.getName().toLowerCase().matches(expression.toLowerCase())) {

                LOGGER.debug("Matched {} to {}. Adding to final filter list.", file.getName().toLowerCase(),
                        expression.toLowerCase());
                
                filteredFiles.add(file);
            }
        }
    }
}
