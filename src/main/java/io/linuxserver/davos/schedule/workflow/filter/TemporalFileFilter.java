package io.linuxserver.davos.schedule.workflow.filter;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class TemporalFileFilter implements FileFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemporalFileFilter.class);

    private DateTime lastRun;
    
    public TemporalFileFilter(DateTime lastRun) {
        this.lastRun = lastRun;
    }
    
    @Override
    public List<FTPFile> filter(List<FTPFile> allFiles) {
        return allFiles.stream().filter(after(lastRun)).collect(toList());
    }

    private Predicate<? super FTPFile> after(DateTime lastRun) {

        LOGGER.debug("Filtering initial set of files by lastRun. Last run was {}", lastRun);
        return f -> f.getLastModified().isAfter(lastRun);
    }
}
