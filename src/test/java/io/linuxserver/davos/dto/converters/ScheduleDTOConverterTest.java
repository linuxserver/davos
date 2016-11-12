package io.linuxserver.davos.dto.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

import io.linuxserver.davos.dto.ScheduleDTO;
import io.linuxserver.davos.persistence.model.ActionModel;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ScheduleDTOConverterTest {

    @Test
    public void shouldConvertAllValues() {
        
        ScheduleModel model = new ScheduleModel();
        
        model.connectionType = TransferProtocol.FTPS;
        model.hostName = "host";
        model.id = 2L;
        model.interval = 3;
        model.lastRun = DateTime.now().toDate();
        model.localFilePath = "Local";
        model.name = "name";
        model.password = "pass";
        model.port = 9090;
        model.remoteFilePath = "remote";
        model.startAutomatically = true;
        model.transferType = FileTransferType.RECURSIVE;
        model.username = "user";
        
        model.actions.add(createActionModel(1L, "move", "destination", null, null, null));
        model.actions.add(createActionModel(2L, "pushbullet", "key", null, null, null));
        
        model.filters.add(createFilterModel(1L, "filter1"));
        model.filters.add(createFilterModel(2L, "filter2"));
        
        ScheduleDTO dto = new ScheduleDTOConverter().convert(model);
        
        assertThat(dto.id).isEqualTo(model.id);
        assertThat(dto.hostName).isEqualTo(model.hostName);
        assertThat(dto.connectionType).isEqualTo(model.connectionType);
        assertThat(dto.interval).isEqualTo(model.interval);
        assertThat(dto.lastRun).isEqualTo(model.lastRun.getTime());
        assertThat(dto.localFilePath).isEqualTo(model.localFilePath);
        assertThat(dto.name).isEqualTo(model.name);
        assertThat(dto.password).isEqualTo(model.password);
        assertThat(dto.port).isEqualTo(model.port);
        assertThat(dto.remoteFilePath).isEqualTo(model.remoteFilePath);
        assertThat(dto.startAutomatically).isEqualTo(model.startAutomatically);
        assertThat(dto.transferType).isEqualTo(model.transferType);
        assertThat(dto.username).isEqualTo(model.username);
        
        assertThat(dto.actions).hasSize(2);
        
        assertThat(dto.actions.get(0).id).isEqualTo(1L);
        assertThat(dto.actions.get(0).actionType).isEqualTo("move");
        assertThat(dto.actions.get(0).f1).isEqualTo("destination");
        assertThat(dto.actions.get(0).f2).isNull();
        assertThat(dto.actions.get(0).f3).isNull();
        assertThat(dto.actions.get(0).f4).isNull();
        
        assertThat(dto.actions.get(1).id).isEqualTo(2L);
        assertThat(dto.actions.get(1).actionType).isEqualTo("pushbullet");
        assertThat(dto.actions.get(1).f1).isEqualTo("key");
        assertThat(dto.actions.get(1).f2).isNull();
        assertThat(dto.actions.get(1).f3).isNull();
        assertThat(dto.actions.get(1).f4).isNull();
        
        assertThat(dto.filters).hasSize(2);
        
        assertThat(dto.filters.get(0).id).isEqualTo(1L);
        assertThat(dto.filters.get(0).value).isEqualTo("filter1");
        
        assertThat(dto.filters.get(1).id).isEqualTo(2L);
        assertThat(dto.filters.get(1).value).isEqualTo("filter2");
    }
    
    @Test
    public void nullDateShouldBeZero() {
        
        ScheduleModel model = new ScheduleModel();
        
        ScheduleDTO dto = new ScheduleDTOConverter().convert(model);
        
        assertThat(dto.lastRun).isEqualTo(model.lastRun.getTime());
    }
    
    private FilterModel createFilterModel(Long id, String value) {
        
        FilterModel model = new FilterModel();
        
        model.id = id;
        model.value = value;
        
        return model;
    }
    
    private ActionModel createActionModel(Long id, String actionType, String f1, String f2, String f3, String f4) {
        
        ActionModel model = new ActionModel();
        
        model.id = id;
        model.actionType = actionType;
        model.f1 = f1;
        model.f2 = f2;
        model.f3 = f3;
        model.f4 = f4;
        
        return model;
    }
}
