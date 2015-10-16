package io.linuxserver.davos.schedule.workflow.steps;

import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public abstract class WorkflowStep {

    protected WorkflowStep nextStep;

    abstract public void runSchedule(ScheduleWorkflow schedule);
}
