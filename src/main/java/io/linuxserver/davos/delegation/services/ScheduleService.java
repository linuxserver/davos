package io.linuxserver.davos.delegation.services;

import java.util.List;

import io.linuxserver.davos.web.Schedule;

public interface ScheduleService {

    void startSchedule(Long id);

    void stopSchedule(Long id);

    void deleteSchedule(Long id);

    List<Schedule> fetchAllSchedules();

    Schedule fetchSchedule(Long id);
    
    Schedule saveSchedule(Schedule schedule);
}
