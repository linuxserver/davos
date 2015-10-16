package io.linuxserver.davos.schedule.workflow.steps;

import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class ConnectWorkflowStep extends WorkflowStep {

    public ConnectWorkflowStep() {
        this.nextStep = new FilterFilesWorkflowStep();
    }
    
    @Override
    public void runSchedule(ScheduleWorkflow schedule) {
    }
}
