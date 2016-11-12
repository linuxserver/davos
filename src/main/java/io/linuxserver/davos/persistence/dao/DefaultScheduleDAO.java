package io.linuxserver.davos.persistence.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.persistence.repository.ScheduleRepository;

@Component
public class DefaultScheduleDAO implements ScheduleDAO {

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
        return configRepository.save(model);
    }
}
