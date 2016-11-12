package io.linuxserver.davos.dto.converters;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import io.linuxserver.davos.dto.ActionDTO;
import io.linuxserver.davos.dto.FilterDTO;
import io.linuxserver.davos.dto.ScheduleDTO;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ScheduleModelConverterTest {

    @Test
    public void shouldConvertAlLValues() {
        
        ScheduleDTO dto = new ScheduleDTO();

        dto.connectionType = TransferProtocol.FTPS;
        dto.hostName = "host";
        dto.id = 2L;
        dto.interval = 3;
        dto.lastRun = DateTime.now().toDate().getTime();
        dto.localFilePath = "Local";
        dto.name = "name";
        dto.password = "pass";
        dto.port = 9090;
        dto.remoteFilePath = "remote";
        dto.startAutomatically = true;
        dto.transferType = FileTransferType.RECURSIVE;
        dto.username = "user";

        dto.actions.add(createActionModel(1L, "move", "destination", null, null, null));
        dto.actions.add(createActionModel(2L, "pushbullet", "key", null, null, null));

        dto.filters.add(createFilterModel(1L, "filter1"));
        dto.filters.add(createFilterModel(2L, "filter2"));

        ScheduleModel model = new ScheduleModelConverter().convert(dto);

        assertThat(model.id).isEqualTo(dto.id);
        assertThat(model.hostName).isEqualTo(dto.hostName);
        assertThat(model.connectionType).isEqualTo(dto.connectionType);
        assertThat(model.interval).isEqualTo(dto.interval);
        assertThat(model.lastRun).isEqualTo(new Date(dto.lastRun));
        assertThat(model.localFilePath).isEqualTo(dto.localFilePath);
        assertThat(model.name).isEqualTo(dto.name);
        assertThat(model.password).isEqualTo(dto.password);
        assertThat(model.port).isEqualTo(dto.port);
        assertThat(model.remoteFilePath).isEqualTo(dto.remoteFilePath);
        assertThat(model.startAutomatically).isEqualTo(dto.startAutomatically);
        assertThat(model.transferType).isEqualTo(dto.transferType);
        assertThat(model.username).isEqualTo(dto.username);

        assertThat(model.actions).hasSize(2);

        assertThat(model.actions.get(0).id).isEqualTo(1L);
        assertThat(model.actions.get(0).actionType).isEqualTo("move");
        assertThat(model.actions.get(0).f1).isEqualTo("destination");
        assertThat(model.actions.get(0).f2).isNull();
        assertThat(model.actions.get(0).f3).isNull();
        assertThat(model.actions.get(0).f4).isNull();
        assertThat(model.actions.get(0).schedule).isEqualTo(model);
        
        assertThat(model.actions.get(1).id).isEqualTo(2L);
        assertThat(model.actions.get(1).actionType).isEqualTo("pushbullet");
        assertThat(model.actions.get(1).f1).isEqualTo("key");
        assertThat(model.actions.get(1).f2).isNull();
        assertThat(model.actions.get(1).f3).isNull();
        assertThat(model.actions.get(1).f4).isNull();
        assertThat(model.actions.get(1).schedule).isEqualTo(model);

        assertThat(model.filters).hasSize(2);

        assertThat(model.filters.get(0).id).isEqualTo(1L);
        assertThat(model.filters.get(0).value).isEqualTo("filter1");
        assertThat(model.filters.get(0).schedule).isEqualTo(model);

        assertThat(model.filters.get(1).id).isEqualTo(2L);
        assertThat(model.filters.get(1).value).isEqualTo("filter2");
        assertThat(model.filters.get(1).schedule).isEqualTo(model);
    }

    private FilterDTO createFilterModel(Long id, String value) {

        FilterDTO model = new FilterDTO();

        model.id = id;
        model.value = value;

        return model;
    }

    private ActionDTO createActionModel(Long id, String actionType, String f1, String f2, String f3, String f4) {

        ActionDTO model = new ActionDTO();

        model.id = id;
        model.actionType = actionType;
        model.f1 = f1;
        model.f2 = f2;
        model.f3 = f3;
        model.f4 = f4;

        return model;
    }
}
