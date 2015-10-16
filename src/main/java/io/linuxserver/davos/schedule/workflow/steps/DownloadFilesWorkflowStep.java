package io.linuxserver.davos.schedule.workflow.steps;

import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class DownloadFilesWorkflowStep extends WorkflowStep {

    public DownloadFilesWorkflowStep() {
        this.nextStep = new DisconnectWorkflowStep();
    }
    
    @Override
    public void runSchedule(ScheduleWorkflow schedule) {
    }
}
