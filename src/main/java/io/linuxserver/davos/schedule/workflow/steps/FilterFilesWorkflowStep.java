package io.linuxserver.davos.schedule.workflow.steps;

import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class FilterFilesWorkflowStep extends WorkflowStep {

    public FilterFilesWorkflowStep() {
        this.nextStep = new DownloadFilesWorkflowStep();
    }
    
    @Override
    public void runSchedule(ScheduleWorkflow schedule) {
    }
}
