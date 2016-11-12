package io.linuxserver.davos.persistence.dao;

import java.util.List;

import io.linuxserver.davos.persistence.model.ScheduleModel;

public interface ScheduleDAO {

    List<ScheduleModel> getAll();

    ScheduleModel fetchSchedule(Long id);

    ScheduleModel updateConfig(ScheduleModel model);
}
