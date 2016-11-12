package io.linuxserver.davos.dto.converters;

import java.util.Date;

import io.linuxserver.davos.dto.ActionDTO;
import io.linuxserver.davos.dto.FilterDTO;
import io.linuxserver.davos.dto.ScheduleDTO;
import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;

public class ScheduleModelConverter implements Converter<ScheduleDTO, ScheduleModel> {

    @Override
    public ScheduleModel convert(ScheduleDTO source) {

        ScheduleModel model = new ScheduleModel();

        model.connectionType = source.connectionType;
        model.hostName = source.hostName;
        model.id = source.id;
        model.interval = source.interval;
        model.lastRun = new Date(source.lastRun);
        model.localFilePath = source.localFilePath;
        model.name = source.name;
        model.password = source.password;
        model.port = source.port;
        model.remoteFilePath = source.remoteFilePath;
        model.startAutomatically = source.startAutomatically;
        model.transferType = source.transferType;
        model.username = source.username;

        for (ActionDTO action : source.actions) {

            ActionModel actionModel = new ActionModel();

            actionModel.id = action.id;
            actionModel.actionType = action.actionType;
            actionModel.f1 = action.f1;
            actionModel.f2 = action.f2;
            actionModel.f3 = action.f3;
            actionModel.f4 = action.f4;
            actionModel.schedule = model;

            model.actions.add(actionModel);
        }

        for (FilterDTO filter : source.filters) {

            FilterModel filterModel = new FilterModel();

            filterModel.id = filter.id;
            filterModel.value = filter.value;
            filterModel.schedule = model;

            model.filters.add(filterModel);
        }

        return model;
    }
}
