package io.linuxserver.davos.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.linuxserver.davos.dto.ScheduleConfigurationDTO;
import io.linuxserver.davos.dto.ScheduleProcessResponse;
import io.linuxserver.davos.dto.converters.ScheduleConfigurationDTOConverter;
import io.linuxserver.davos.dto.converters.ScheduleConfigurationModelConverter;
import io.linuxserver.davos.exception.ScheduleAlreadyRunningException;
import io.linuxserver.davos.exception.ScheduleNotRunningException;
import io.linuxserver.davos.logging.LoggingManager;
import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.schedule.ScheduleExecutor;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleAPIController.class);

    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    @Resource
    private ScheduleExecutor scheduleExecutor;

    @RequestMapping(value = "/{id}")
    public ScheduleConfigurationDTO getScheduleConfig(@PathVariable("id") Long id) {
        return toDTO(scheduleConfigurationDAO.getConfig(id));
    }

    @RequestMapping(value = "/{id}/stop")
    public ScheduleProcessResponse stopScheduleConfig(@PathVariable("id") Long id) {

        ScheduleProcessResponse scheduleProcessResponse = new ScheduleProcessResponse();
        scheduleProcessResponse.id = id;

        try {
            scheduleExecutor.stopSchedule(id);
            scheduleProcessResponse.message = "Schedule stopped";
        } catch (ScheduleNotRunningException e) {
            LOGGER.info("Unable to stop schedule {} as it was not initially running", id);
            scheduleProcessResponse.message = "Unable to stop schedule. Wasn't running.";
        }

        return scheduleProcessResponse;
    }

    @RequestMapping(value = "/{id}/start")
    public ScheduleProcessResponse startScheduleConfig(@PathVariable("id") Long id) {

        ScheduleProcessResponse scheduleProcessResponse = new ScheduleProcessResponse();
        scheduleProcessResponse.id = id;
        
        try {
            scheduleExecutor.startSchedule(id);
            scheduleProcessResponse.message = "Schedule started";
        } catch (ScheduleAlreadyRunningException e) {
            LOGGER.info("Not starting schedule {} as it is already running", id);
            scheduleProcessResponse.message = "Unable to start schedule. May already be running";
        }

        return scheduleProcessResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ScheduleConfigurationDTO createScheduleConfig(@RequestBody ScheduleConfigurationDTO dto) {

        ScheduleConfigurationModel model = new ScheduleConfigurationModelConverter().convert(dto);
        return toDTO(scheduleConfigurationDAO.updateConfig(model));
    }

    @RequestMapping(value = "/schedules")
    public List<ScheduleConfigurationDTO> getAllSchedules() {
        return scheduleConfigurationDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = "/debug/{debugEnabled}")
    public boolean updateSettings(@PathVariable("debugEnabled") boolean debugEnabled) {
        
        if (debugEnabled)
            LoggingManager.enableDebug();
        
        else
            LoggingManager.disableDebug();
        
        return true;
    }
    
    private ScheduleConfigurationDTO toDTO(ScheduleConfigurationModel model) {
        return new ScheduleConfigurationDTOConverter().convert(model);
    }
}
