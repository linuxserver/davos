package io.linuxserver.davos.schedule.workflow.filter;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class ReferentialFileFilter implements FileFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferentialFileFilter.class);

    private List<String> filesToCompareWith = new ArrayList<>();

    public ReferentialFileFilter(List<String> files) {
        filesToCompareWith = files;
    }

    @Override
    public List<FTPFile> filter(List<FTPFile> allFiles) {

        if (filesToCompareWith.isEmpty()) {
            LOGGER.debug("No files in last scan. Using all files in this scan for filtering");
            return allFiles;
        }

        LOGGER.debug("Files in last scan {}", filesToCompareWith);
        LOGGER.debug("Files in this scan {}", allFiles.stream().map(f -> f.getName()).collect(toList()));
        
        LOGGER.debug("Checking this scan for new files - comparing with files from last scan");
        List<FTPFile> collectedFiles = allFiles.stream().filter(f -> !filesToCompareWith.contains(f.getName())).collect(toList());
        LOGGER.debug("New files {}", collectedFiles.stream().map(f -> f.getName()).collect(toList()));
        
        return collectedFiles;
    }
}
