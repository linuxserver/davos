package io.linuxserver.davos.schedule.workflow;

public class FilterFilesWorkflowStep extends WorkflowStep {

    public FilterFilesWorkflowStep() {
        this.nextStep = new DownloadFilesWorkflowStep();
    }
    
    @Override
    public void runStep(ScheduleWorkflow schedule) {
    }
}
