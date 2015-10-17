package io.linuxserver.davos.schedule.workflow;

public abstract class WorkflowStep {

    protected WorkflowStep nextStep;

    abstract public void runStep(ScheduleWorkflow schedule);
}
