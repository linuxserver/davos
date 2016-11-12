package io.linuxserver.davos.persistence.dao;

import java.util.List;

import org.joda.time.DateTime;

import io.linuxserver.davos.persistence.model.ScheduleModel;

public interface ScheduleConfigurationDAO {

    List<ScheduleModel> getAll();

    ScheduleModel getConfig(Long id);

    ScheduleModel updateConfig(ScheduleModel model);
    
    void updateLastRun(Long configId, DateTime lastRun);
}
