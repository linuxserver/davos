package io.linuxserver.davos.schedule.workflow;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.schedule.workflow.filter.ReferentialFileFilter;
import io.linuxserver.davos.schedule.workflow.transfer.FTPTransfer;
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

            List<String> filters = schedule.getConfig().getFilters();

            List<FTPFile> allFiles = schedule.getConnection().listFiles(schedule.getConfig().getRemoteFilePath()).stream()
                    .filter(removeCurrentAndParentDirs()).collect(toList());
            List<FTPFile> filesToFilter = new ReferentialFileFilter(schedule.getFilesFromLastScan()).filter(allFiles);
            List<FTPFile> filteredFiles = new ArrayList<FTPFile>();

            LOGGER.debug("Clearing pending download list");
            schedule.getFilesToDownload().clear();

            if (filters.isEmpty() && !schedule.getConfig().isFiltersMandatory()) {

                LOGGER.info("Filter list was empty. Adding all found files to list");
                LOGGER.debug("All files: {}", filesToFilter.stream().map(f -> f.getName()).collect(Collectors.toList()));
                schedule.getFilesToDownload().addAll(filesToFilter.stream().map(f -> new FTPTransfer(f)).collect(toList()));

            } else {

                LOGGER.debug("Filters used {}", filters);
                LOGGER.debug("Files to filter against {}", filteredFiles.stream().map(f -> f.getName()).collect(toList()));

                for (FTPFile file : filesToFilter)
                    filterFilesByName(filters, filteredFiles, file);

                schedule.getFilesToDownload().addAll(filteredFiles.stream().map(f -> new FTPTransfer(f)).collect(toList()));
            }

            LOGGER.debug("Resetting files from scan to files in this scan");
            schedule.getFilesFromLastScan().clear();
            schedule.getFilesFromLastScan().addAll(allFiles.stream().map(f -> f.getName()).collect(toList()));
            LOGGER.debug("Files from last scan set to {}", schedule.getFilesFromLastScan());

            LOGGER.info("Filtered files. Moving onto next step");
            nextStep.runStep(schedule);

        } catch (FTPException e) {

            LOGGER.error("Unable to filter files. Error message was: {}", e.getMessage());
            LOGGER.debug("Stacktrace", e);

            LOGGER.info("Backing out of this run.");
            backoutStep.runStep(schedule);
        }
    }

    private void filterFilesByName(List<String> filters, List<FTPFile> filteredFiles, FTPFile file) {

        for (String filter : filters) {

            String expression = PatternBuilder.buildFromFilterString(filter);

            if (file.getName().toLowerCase().matches(expression.toLowerCase())) {

                LOGGER.debug("Matched {} to {}. Adding to final filter list.", file.getName().toLowerCase(),
                        expression.toLowerCase());

                filteredFiles.add(file);

                return;
            }
        }
    }

    private Predicate<? super FTPFile> removeCurrentAndParentDirs() {
        return file -> !file.getName().equals(".") && !file.getName().equals("..");
    }
}
