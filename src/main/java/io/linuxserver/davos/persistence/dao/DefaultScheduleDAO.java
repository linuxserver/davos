package io.linuxserver.davos.persistence.dao;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.persistence.repository.ScheduleRepository;

@Component
public class DefaultScheduleDAO implements ScheduleDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScheduleDAO.class);
    
    @Resource
    private ScheduleRepository configRepository;

    @Override
    public List<ScheduleModel> getAll() {
        return configRepository.findAll();
    }

    @Override
    public ScheduleModel fetchSchedule(Long id) {
        return configRepository.findOne(id);
    }

    @Override
    public ScheduleModel updateConfig(ScheduleModel model) {
        ScheduleModel savedModel = configRepository.save(model);
        LOGGER.debug("Schedule model has been saved. Returned values from DB are: {}", model);
        return savedModel;
    }
}
