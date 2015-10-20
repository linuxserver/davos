package io.linuxserver.davos.persistence.dao;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.persistence.repository.ScheduleConfigurationRepository;

@Component
public class DefaultScheduleConfigurationDAO implements ScheduleConfigurationDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScheduleConfigurationDAO.class);
    
    @Resource
    private ScheduleConfigurationRepository configRepository;

    @Override
    public List<ScheduleConfigurationModel> getAll() {
        return configRepository.findAll();
    }

    @Override
    public ScheduleConfigurationModel getConfig(Long id) {
        return configRepository.findOne(id);
    }

    @Override
    public ScheduleConfigurationModel updateConfig(ScheduleConfigurationModel model) {
        return configRepository.save(model);
    }

    @Override
    public void updateLastRun(Long configId, DateTime lastRun) {
        
        LOGGER.debug("Updating model with new lastRun of {}", lastRun);
        ScheduleConfigurationModel model = configRepository.findOne(configId);
        LOGGER.debug("Got existing model, {}", model);
        
        model.lastRun = lastRun.toDate();
        configRepository.save(model);
    }
}
