package io.linuxserver.davos.schedule;

import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.schedule.workflow.actions.MoveFileAction;
import io.linuxserver.davos.schedule.workflow.actions.PushbulletNotifyAction;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;

public class ScheduleConfigurationFactory {

    public static ScheduleConfiguration createConfig(ScheduleConfigurationModel model) {

        ScheduleConfiguration config = new ScheduleConfiguration(model.name, model.connectionType, model.hostName, model.port,
                new UserCredentials(model.username, model.password), model.remoteFilePath, model.localFilePath,
                model.transferType);

        if (null != model.lastRun)
            config.setLastRun(model.lastRun);

        if (null != model.filters)
            addFilters(model, config);

        if (null != model.actions)
            addActions(model, config);

        return config;
    }

    private static void addActions(ScheduleConfigurationModel model, ScheduleConfiguration config) {

        for (ActionModel action : model.actions) {

            if ("move".equals(action.actionType))
                config.getActions().add(new MoveFileAction(action.f1, action.f2));

            if ("pushbullet".equals(action.actionType))
                config.getActions().add(new PushbulletNotifyAction(action.f1));
        }
    }

    private static void addFilters(ScheduleConfigurationModel model, ScheduleConfiguration config) {

        for (FilterModel filter : model.filters)
            config.getFilters().add(filter.value);
    }
}
