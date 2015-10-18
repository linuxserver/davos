package io.linuxserver.davos.schedule;

import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.schedule.workflow.ScheduleWorkflow;

public class RunnableSchedule implements Runnable {

    private ScheduleConfigurationDAO configurationDAO;
    private Long scheduleId;

    public RunnableSchedule(Long scheduleId, ScheduleConfigurationDAO configurationDAO) {

        this.scheduleId = scheduleId;
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void run() {

        ScheduleConfigurationModel model = configurationDAO.getConfig(scheduleId);
        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);
        ScheduleWorkflow scheduleWorkflow = new ScheduleWorkflow(config);

        scheduleWorkflow.setConfigurationDAO(configurationDAO);
        scheduleWorkflow.start();
    }
}
