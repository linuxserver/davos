package io.linuxserver.davos.schedule.workflow;

import java.util.ArrayList;
import java.util.List;

import io.linuxserver.davos.transfer.ftp.FTPFile;

public class DownloadFilesWorkflowStep extends WorkflowStep {

    private List<FTPFile> filesToDownload = new ArrayList<FTPFile>();
    
    public DownloadFilesWorkflowStep() {
        this.nextStep = new DisconnectWorkflowStep();
    }
    
    @Override
    public void runStep(ScheduleWorkflow schedule) {
    }

    public void setFilesToDownload(List<FTPFile> filesToDownload) {
        this.filesToDownload = filesToDownload;
    }
}
