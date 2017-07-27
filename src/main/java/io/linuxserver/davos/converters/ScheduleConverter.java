package io.linuxserver.davos.converters;

import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.web.API;
import io.linuxserver.davos.web.Filter;
import io.linuxserver.davos.web.Pushbullet;
import io.linuxserver.davos.web.SNS;
import io.linuxserver.davos.web.Schedule;
import io.linuxserver.davos.web.selectors.MethodSelector;
import io.linuxserver.davos.web.selectors.TransferSelector;

@Component
public class ScheduleConverter implements Converter<ScheduleModel, Schedule> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleConverter.class);
    
    @Override
    public Schedule convertTo(ScheduleModel source) {

        Schedule schedule = new Schedule();

        schedule.setId(source.id);
        schedule.setInterval(source.interval);
        schedule.setLocalDirectory(source.localFilePath);
        schedule.setName(source.name);
        schedule.setHostDirectory(source.remoteFilePath);
        schedule.setAutomatic(source.getStartAutomatically());
        schedule.setHost(source.host.id);
        schedule.setTransferType(TransferSelector.valueOf(source.transferType.toString()));
        schedule.setMoveFileTo(source.moveFileTo);
        schedule.getLastScannedFiles().addAll(source.scannedFiles.stream().map(f -> f.file).collect(toList()));
        schedule.setFiltersMandatory(source.getFiltersMandatory());
        schedule.setDeleteHostFile(source.getDeleteHostFile());
        schedule.setInvertFilters(source.getInvertFilters());
        
        for (ActionModel action : source.actions) {

            if ("api".equals(action.actionType)) {

                API api = new API();
                api.setId(action.id);
                api.setUrl(action.f1);
                api.setMethod(MethodSelector.valueOf(action.f2));
                api.setContentType(action.f3);
                api.setBody(action.f4);

                schedule.getApis().add(api);

            } else if ("pushbullet".equals(action.actionType)) {

                Pushbullet notification = new Pushbullet();
                notification.setId(action.id);
                notification.setApiKey(action.f1);

                schedule.getNotifications().getPushbullet().add(notification);
                
            } else if ("sns".equals(action.actionType)) {
                
                SNS sns = new SNS();
                sns.setId(action.id);
                sns.setTopicArn(action.f1);
                sns.setRegion(action.f2);
                sns.setAccessKey(action.f3);
                sns.setSecretAccessKey(action.f4);
                
                schedule.getNotifications().getSns().add(sns);
            }
        }

        for (FilterModel filter : source.filters) {

            Filter filterDto = new Filter();

            filterDto.setId(filter.id);
            filterDto.setValue(filter.value);

            schedule.getFilters().add(filterDto);
        }

        return schedule;
    }

    @Override
    public ScheduleModel convertFrom(Schedule source) {

        ScheduleModel model = new ScheduleModel();

        model.id = source.getId();
        model.name = source.getName();
        model.interval = source.getInterval();
        model.localFilePath = source.getLocalDirectory();
        model.remoteFilePath = source.getHostDirectory();
        model.setStartAutomatically(source.isAutomatic());
        model.transferType = FileTransferType.valueOf(source.getTransferType().toString());
        model.moveFileTo = source.getMoveFileTo();
        model.setFiltersMandatory(source.isFiltersMandatory());
        model.setInvertFilters(source.isInvertFilters());
        model.setDeleteHostFile(source.isDeleteHostFile());
        
        if (StringUtils.isNotBlank(source.getMoveFileTo())) {

            LOGGER.debug("Converting MoveTo to internal action: {}", source.getMoveFileTo());
            
            ActionModel moveTo = new ActionModel();
            moveTo.actionType = "move";
            moveTo.f1 = source.getMoveFileTo();
            moveTo.schedule = model;

            model.actions.add(moveTo);
        }

        for (Pushbullet action : source.getNotifications().getPushbullet()) {

            LOGGER.debug("Converting Pushbullet to internal action: {}", action.getApiKey());
            
            ActionModel actionModel = new ActionModel();
            actionModel.id = action.getId();
            actionModel.actionType = "pushbullet";
            actionModel.f1 = action.getApiKey();
            actionModel.schedule = model;

            model.actions.add(actionModel);
        }
        
        for (SNS action : source.getNotifications().getSns()) {

            LOGGER.debug("Converting SNS to internal action: {}", action.getTopicArn());
                
            ActionModel actionModel = new ActionModel();
            actionModel.id = action.getId();
            actionModel.actionType = "sns";
            actionModel.f1 = action.getTopicArn();
            actionModel.f2 = action.getRegion();
            actionModel.f3 = action.getAccessKey();
            actionModel.f4 = action.getSecretAccessKey();
            actionModel.schedule = model;

            model.actions.add(actionModel);
        }

        for (API action : source.getApis()) {

            LOGGER.debug("Converting API to internal action: {}", action.getUrl());
            
            ActionModel actionModel = new ActionModel();
            actionModel.id = action.getId();
            actionModel.actionType = "api";
            actionModel.f1 = action.getUrl();
            actionModel.f2 = action.getMethod().toString();
            actionModel.f3 = action.getContentType();
            actionModel.f4 = action.getBody();
            actionModel.schedule = model;

            model.actions.add(actionModel);
        }

        for (Filter filter : source.getFilters()) {

            FilterModel filterModel = new FilterModel();

            filterModel.id = filter.getId();
            filterModel.value = filter.getValue();
            filterModel.schedule = model;

            model.filters.add(filterModel);
        }

        return model;
    }
}
