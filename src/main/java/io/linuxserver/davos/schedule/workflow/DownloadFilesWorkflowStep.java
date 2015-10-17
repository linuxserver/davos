package io.linuxserver.davos.schedule.workflow;

public class DownloadFilesWorkflowStep extends WorkflowStep {

    public DownloadFilesWorkflowStep() {
        this.nextStep = new DisconnectWorkflowStep();
    }
    
    @Override
    public void runStep(ScheduleWorkflow schedule) {
    }
}
