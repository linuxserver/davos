package io.linuxserver.davos.schedule;

import org.apache.commons.lang3.StringUtils;

import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.workflow.actions.HttpAPICallAction;
import io.linuxserver.davos.schedule.workflow.actions.MoveFileAction;
import io.linuxserver.davos.schedule.workflow.actions.PushbulletNotifyAction;
import io.linuxserver.davos.schedule.workflow.actions.SNSNotifyAction;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials;
import io.linuxserver.davos.transfer.ftp.client.UserCredentials.Identity;

public class ScheduleConfigurationFactory {

    public static ScheduleConfiguration createConfig(ScheduleModel model) {

        ScheduleConfiguration config = new ScheduleConfiguration(model.name, model.host.protocol, model.host.address,
                model.host.port, buildCredentials(model.host), model.remoteFilePath, model.localFilePath, model.transferType,
                model.getFiltersMandatory(), model.getInvertFilters(), model.getDeleteHostFile());

        if (StringUtils.isNotBlank(model.moveFileTo))
            config.getActions().add(new MoveFileAction(config.getLocalFilePath(), model.moveFileTo));

        if (null != model.filters)
            addFilters(model, config);

        if (null != model.actions)
            addActions(model, config);

        return config;
    }

    private static UserCredentials buildCredentials(HostModel host) {

        if (host.isIdentityFileEnabled())
            return new UserCredentials(host.username, new Identity(host.identityFile));

        return new UserCredentials(host.username, host.password);
    }

    private static void addActions(ScheduleModel model, ScheduleConfiguration config) {

        for (ActionModel action : model.actions) {

            if ("pushbullet".equals(action.actionType))
                config.getActions().add(new PushbulletNotifyAction(action.f1));
            
            if ("sns".equals(action.actionType))
                config.getActions().add(new SNSNotifyAction(action.f2, action.f1, action.f3, action.f4));

            if ("api".equals(action.actionType))
                config.getActions().add(new HttpAPICallAction(action.f1, action.f2, action.f3, action.f4));
        }
    }

    private static void addFilters(ScheduleModel model, ScheduleConfiguration config) {

        for (FilterModel filter : model.filters)
            config.getFilters().add(filter.value);
    }
}
