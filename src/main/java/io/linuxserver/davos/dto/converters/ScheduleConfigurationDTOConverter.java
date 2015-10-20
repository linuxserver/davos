package io.linuxserver.davos.dto.converters;

import io.linuxserver.davos.dto.ActionDTO;
import io.linuxserver.davos.dto.FilterDTO;
import io.linuxserver.davos.dto.ScheduleConfigurationDTO;
import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

public class ScheduleConfigurationDTOConverter implements Converter<ScheduleConfigurationModel, ScheduleConfigurationDTO> {

    @Override
    public ScheduleConfigurationDTO convert(ScheduleConfigurationModel source) {

        ScheduleConfigurationDTO dto = new ScheduleConfigurationDTO();

        dto.connectionType = source.connectionType;
        dto.hostName = source.hostName;
        dto.id = source.id;
        dto.interval = source.interval;
        dto.lastRun = source.lastRun;
        dto.localFilePath = source.localFilePath;
        dto.name = source.name;
        dto.password = source.password;
        dto.port = source.port;
        dto.remoteFilePath = source.remoteFilePath;
        dto.startAutomatically = source.startAutomatically;
        dto.transferType = source.transferType;
        dto.username = source.username;

        for (ActionModel action : source.actions) {

            ActionDTO actionDto = new ActionDTO();

            actionDto.id = action.id;
            actionDto.actionType = action.actionType;
            actionDto.f1 = action.f1;
            actionDto.f2 = action.f2;
            actionDto.f3 = action.f3;
            actionDto.f4 = action.f4;

            dto.actions.add(actionDto);
        }

        for (FilterModel filter : source.filters) {

            FilterDTO filterDto = new FilterDTO();

            filterDto.id = filter.id;
            filterDto.value = filter.value;

            dto.filters.add(filterDto);
        }

        return dto;
    }
}
