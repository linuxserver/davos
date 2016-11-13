package io.linuxserver.davos.delegation.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.converters.HostConverter;
import io.linuxserver.davos.converters.ScheduleConverter;
import io.linuxserver.davos.persistence.dao.HostDAO;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.ScheduleExecutor;
import io.linuxserver.davos.web.Schedule;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    
    @Resource
    private ScheduleConverter scheduleConverter;

    @Resource
    private ScheduleExecutor scheduleExecutor;

    @Resource
    private HostConverter hostConverter;

    @Resource
    private ScheduleDAO scheduleDAO;

    @Resource
    private HostDAO hostDAO;

    @Override
    public void startSchedule(Long id) {

        LOGGER.info("Starting schedule");
        scheduleExecutor.startSchedule(id);
        LOGGER.info("Schedule started");
    }

    @Override
    public void stopSchedule(Long id) {
        
        LOGGER.info("Stopping schedule");
        scheduleExecutor.stopSchedule(id);
        LOGGER.info("Schedule stopped");
    }

    @Override
    public void deleteSchedule(Long id) {
        
        if (scheduleExecutor.isScheduleRunning(id)) {
            
            LOGGER.debug("Schedule is running, so will stop it before deleting");
            stopSchedule(id);
        }
        
        scheduleDAO.deleteSchedule(id);
    }

    @Override
    public List<Schedule> fetchAllSchedules() {
        return scheduleDAO.getAll().stream().map(this::toSchedule).collect(Collectors.toList());
    }

    @Override
    public Schedule fetchSchedule(Long id) {
        return toSchedule(scheduleDAO.fetchSchedule(id));
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {

        HostModel hostModel = hostDAO.fetchHost(schedule.getHost());
        ScheduleModel model = scheduleConverter.convertFrom(schedule);
        model.host = hostModel;

        ScheduleModel updatedModel = scheduleDAO.updateConfig(model);

        return scheduleConverter.convertTo(updatedModel);
    }

    private Schedule toSchedule(ScheduleModel model) {

        Schedule convertTo = scheduleConverter.convertTo(model);

        if (scheduleExecutor.isScheduleRunning(convertTo.getId()))
            convertTo.setRunning(true);

        return convertTo;
    }
}
