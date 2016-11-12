package io.linuxserver.davos.persistence.dao;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.persistence.repository.ScheduleConfigurationRepository;

@Component
public class DefaultScheduleConfigurationDAO implements ScheduleConfigurationDAO {

    @Resource
    private ScheduleConfigurationRepository configRepository;

    @Override
    public List<ScheduleModel> getAll() {
        return configRepository.findAll();
    }

    @Override
    public ScheduleModel getConfig(Long id) {
        return configRepository.findOne(id);
    }

    @Override
    public ScheduleModel updateConfig(ScheduleModel model) {
        return configRepository.save(model);
    }

    @Override
    public void updateLastRun(Long configId, DateTime lastRun) {
        
        ScheduleModel model = getConfig(configId);
        
        model.lastRun = lastRun.toDate();
        configRepository.save(model);
    }
}
