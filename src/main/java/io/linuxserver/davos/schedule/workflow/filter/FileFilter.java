package io.linuxserver.davos.schedule.workflow.filter;

import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public interface FileFilter {

    List<FTPFile> filter(List<FTPFile> allFiles);
}
