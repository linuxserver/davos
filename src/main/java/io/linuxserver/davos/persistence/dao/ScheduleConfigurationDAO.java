package io.linuxserver.davos.persistence.dao;

import java.util.List;

import org.joda.time.DateTime;

import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

public interface ScheduleConfigurationDAO {

    List<ScheduleConfigurationModel> getAll();

    ScheduleConfigurationModel getConfig(Long id);

    ScheduleConfigurationModel updateConfig(ScheduleConfigurationModel model);
    
    void updateLastRun(Long configId, DateTime lastRun);
}
