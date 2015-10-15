package io.linuxserver.davos.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

public interface ScheduleConfigurationRepository extends CrudRepository<ScheduleConfigurationModel, Long> {

    List<ScheduleConfigurationModel> findAll();
}
