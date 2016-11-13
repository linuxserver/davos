package io.linuxserver.davos.persistence.dao;

import java.util.List;

import io.linuxserver.davos.persistence.model.ScheduleModel;

public interface ScheduleDAO {

    List<ScheduleModel> getAll();

    List<ScheduleModel> fetchSchedulesUsingHost(Long hostId);
    
    ScheduleModel fetchSchedule(Long id);

    ScheduleModel updateConfig(ScheduleModel model);
    
    void updateScannedFilesOnSchedule(Long id, List<String> newlyScannedFiles);

    void deleteSchedule(Long id);
}
