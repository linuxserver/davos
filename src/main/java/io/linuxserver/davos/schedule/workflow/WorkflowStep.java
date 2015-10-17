package io.linuxserver.davos.schedule.workflow;

public abstract class WorkflowStep {

    protected WorkflowStep nextStep;
    protected WorkflowStep backoutStep;

    abstract public void runStep(ScheduleWorkflow schedule);
}
