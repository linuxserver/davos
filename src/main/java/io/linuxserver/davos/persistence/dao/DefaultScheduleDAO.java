package io.linuxserver.davos.persistence.dao;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ScannedFileModel;
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

        if (null != model.id) {

            LOGGER.debug("Getting original view of schedule to overlay scannedFiles. "
                    + "These should only be updated by 'updateScannedFilesOnSchedule'");
            ScheduleModel current = fetchSchedule(model.id);
            model.scannedFiles = current.scannedFiles;
        }

        LOGGER.debug("Saving model: {}, filters {}", model, model.filters);
        ScheduleModel savedModel = configRepository.save(model);
        LOGGER.debug("Schedule model has been saved. Returned values from DB are: {}", model);
        return savedModel;
    }

    @Override
    public List<ScheduleModel> fetchSchedulesUsingHost(Long hostId) {

        List<ScheduleModel> models = configRepository.findByHost_Id(hostId);
        LOGGER.debug("Found {} schedules using host {}", models.size(), hostId);
        return models;
    }

    @Override
    public void updateScannedFilesOnSchedule(Long id, List<String> newlyScannedFiles) {

        ScheduleModel model = configRepository.findOne(id);

        model.scannedFiles.clear();
        model.scannedFiles.addAll(newlyScannedFiles.stream().map(f -> toScannedFileModel(f, model)).collect(toList()));

        configRepository.save(model);
    }

    private ScannedFileModel toScannedFileModel(String fileName, ScheduleModel model) {

        ScannedFileModel scannedFileModel = new ScannedFileModel();
        scannedFileModel.file = fileName;
        scannedFileModel.schedule = model;

        return scannedFileModel;
    }

    @Override
    public void deleteSchedule(Long id) {
        configRepository.delete(id);
        LOGGER.info("Schedule has been deleted");
    }
}
