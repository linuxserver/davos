package io.linuxserver.davos.web;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

@RestController
@RequestMapping("/config")
public class RestConfigurationController {

    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    @RequestMapping(value = "/schedule/{id}")
    public ScheduleConfigurationModel getScheduleConfig(@PathVariable("id") Long id) {
        return scheduleConfigurationDAO.getConfig(id);
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ScheduleConfigurationModel createScheduleConfig(@RequestBody ScheduleConfigurationModel model) {
        return scheduleConfigurationDAO.updateConfig(model);
    }
}
