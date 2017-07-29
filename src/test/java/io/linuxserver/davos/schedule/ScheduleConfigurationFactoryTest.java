package io.linuxserver.davos.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.workflow.actions.HttpAPICallAction;
import io.linuxserver.davos.schedule.workflow.actions.MoveFileAction;
import io.linuxserver.davos.schedule.workflow.actions.PushbulletNotifyAction;
import io.linuxserver.davos.schedule.workflow.actions.SNSNotifyAction;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ScheduleConfigurationFactoryTest {

    @Test
    public void shouldConvertAllMainFields() {

        ScheduleModel model = new ScheduleModel();

        model.host = new HostModel();
        model.host.protocol = TransferProtocol.FTP;
        model.host.address = "hostname";
        model.host.password = "password";
        model.host.port = 8;
        model.host.username = "username";
        model.setFiltersMandatory(true);
        model.localFilePath = "local/";
        model.name = "schedulename";
        model.remoteFilePath = "thing/";
        model.setStartAutomatically(true);
        model.transferType = FileTransferType.FILE;

        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);

        assertThat(config.getConnectionType()).isEqualTo(model.host.protocol);
        assertThat(config.getHostName()).isEqualTo(model.host.address);
        assertThat(config.getLocalFilePath()).isEqualTo(model.localFilePath);
        assertThat(config.getScheduleName()).isEqualTo(model.name);
        assertThat(config.getCredentials().getPassword()).isEqualTo(model.host.password);
        assertThat(config.getCredentials().getIdentity()).isNull();
        assertThat(config.getPort()).isEqualTo(model.host.port);
        assertThat(config.getRemoteFilePath()).isEqualTo(model.remoteFilePath);
        assertThat(config.getTransferType()).isEqualTo(model.transferType);
        assertThat(config.getCredentials().getUsername()).isEqualTo(model.host.username);
        assertThat(config.isFiltersMandatory()).isTrue();
    }
    
    @Test
    public void shouldUseCorrectCredentialsIfIdentityPresent() {

        ScheduleModel model = new ScheduleModel();

        model.host = new HostModel();
        model.host.protocol = TransferProtocol.FTP;
        model.host.address = "hostname";
        model.host.password = "password";
        model.host.port = 8;
        model.host.username = "username";
        model.host.setIdentityFileEnabled(true);
        model.host.identityFile = "blah";
        model.setFiltersMandatory(true);
        model.localFilePath = "local/";
        model.name = "schedulename";
        model.remoteFilePath = "thing/";
        model.setStartAutomatically(true);
        model.transferType = FileTransferType.FILE;

        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);

        assertThat(config.getConnectionType()).isEqualTo(model.host.protocol);
        assertThat(config.getHostName()).isEqualTo(model.host.address);
        assertThat(config.getLocalFilePath()).isEqualTo(model.localFilePath);
        assertThat(config.getScheduleName()).isEqualTo(model.name);
        assertThat(config.getCredentials().getPassword()).isNull();
        assertThat(config.getCredentials().getIdentity().getIdentityFile()).isEqualTo("blah");
        assertThat(config.getPort()).isEqualTo(model.host.port);
        assertThat(config.getRemoteFilePath()).isEqualTo(model.remoteFilePath);
        assertThat(config.getTransferType()).isEqualTo(model.transferType);
        assertThat(config.getCredentials().getUsername()).isEqualTo(model.host.username);
        assertThat(config.isFiltersMandatory()).isTrue();
    }

    @Test
    public void shouldAddAllFiltersIfAny() {

        ScheduleModel model = new ScheduleModel();

        model.host = new HostModel();
        model.host.protocol = TransferProtocol.FTP;
        model.host.address = "hostname";
        model.host.password = "password";
        model.host.port = 8;
        model.host.username = "username";

        model.filters = new ArrayList<FilterModel>();

        FilterModel filterModel = new FilterModel();
        filterModel.value = "filter1";

        FilterModel filterModel2 = new FilterModel();
        filterModel2.value = "filter2";

        model.filters.add(filterModel);
        model.filters.add(filterModel2);

        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);

        assertThat(config.getFilters()).contains("filter1", "filter2");
        assertThat(config.getFilters()).hasSize(2);
    }

    @Test
    public void shouldAddAllActionsIfAny() {

        ScheduleModel model = new ScheduleModel();

        model.host = new HostModel();
        model.host.protocol = TransferProtocol.FTP;
        model.host.address = "hostname";
        model.host.password = "password";
        model.host.port = 8;
        model.host.username = "username";
        
        model.localFilePath = "a/local/path/";
        model.moveFileTo = "/local/path";
        model.actions = new ArrayList<ActionModel>();

        ActionModel action2 = new ActionModel();
        action2.actionType = "pushbullet";
        action2.f1 = "apiKey";

        ActionModel action3 = new ActionModel();
        action3.actionType = "api";
        action3.f1 = "url";
        action3.f2 = "POST";
        action3.f3 = "application/json";
        action3.f4 = "some body";
        
        ActionModel action4 = new ActionModel();
        action4.actionType = "sns";
        action4.f1 = "topic";
        action4.f2 = "region";
        action4.f3 = "Access";
        action4.f4 = "secret";

        model.actions.add(action2);
        model.actions.add(action3);
        model.actions.add(action4);

        ScheduleConfiguration config = ScheduleConfigurationFactory.createConfig(model);

        assertThat(config.getActions().get(0)).isInstanceOf(MoveFileAction.class);
        assertThat(config.getActions().get(1)).isInstanceOf(PushbulletNotifyAction.class);
        assertThat(config.getActions().get(2)).isInstanceOf(HttpAPICallAction.class);
        assertThat(config.getActions().get(3)).isInstanceOf(SNSNotifyAction.class);
    }
}
