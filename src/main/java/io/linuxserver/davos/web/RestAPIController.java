package io.linuxserver.davos.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.linuxserver.davos.dto.ScheduleConfigurationDTO;
import io.linuxserver.davos.dto.converters.ScheduleConfigurationDTOConverter;
import io.linuxserver.davos.dto.converters.ScheduleConfigurationModelConverter;
import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

@RestController
@RequestMapping("/api/v1")
public class RestAPIController {

    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    @RequestMapping(value = "/schedule/{id}")
    public ScheduleConfigurationDTO getScheduleConfig(@PathVariable("id") Long id) {
        return toDTO(scheduleConfigurationDAO.getConfig(id));
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ScheduleConfigurationDTO createScheduleConfig(@RequestBody ScheduleConfigurationDTO dto) {

        ScheduleConfigurationModel model = new ScheduleConfigurationModelConverter().convert(dto);
        return toDTO(scheduleConfigurationDAO.updateConfig(model));
    }

    @RequestMapping(value = "/schedules")
    public List<ScheduleConfigurationDTO> getAllSchedules() {
        return scheduleConfigurationDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ScheduleConfigurationDTO toDTO(ScheduleConfigurationModel model) {
        return new ScheduleConfigurationDTOConverter().convert(model);
    }
}
