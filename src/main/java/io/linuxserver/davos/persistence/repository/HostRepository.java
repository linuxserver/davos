package io.linuxserver.davos.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.linuxserver.davos.persistence.model.HostModel;

public interface HostRepository extends CrudRepository<HostModel, Long> {

    List<HostModel> findAll();
}
