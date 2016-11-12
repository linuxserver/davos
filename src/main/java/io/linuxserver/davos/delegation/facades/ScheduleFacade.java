package io.linuxserver.davos.delegation.facades;

import java.util.List;

import io.linuxserver.davos.dto.ScheduleDTO;

public interface ScheduleFacade {

    void startSchedule(Long id);

    void stopSchedule(Long id);

    void deleteSchedule(Long id);

    List<ScheduleDTO> fetchAllSchedules();

    ScheduleDTO fetchSchedule(Long id);
    
    ScheduleDTO saveSchedule(ScheduleDTO schedule);
    
    ScheduleDTO createBlankSchedule();
}
