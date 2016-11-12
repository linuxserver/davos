package io.linuxserver.davos.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.linuxserver.davos.delegation.services.HostService;
import io.linuxserver.davos.delegation.services.ScheduleService;
import io.linuxserver.davos.web.Host;
import io.linuxserver.davos.web.Schedule;
import io.linuxserver.davos.web.controller.response.APIResponse;

public class APIControllerTest {

    @InjectMocks
    private APIController controller = new APIController();

    @Mock
    private ScheduleService mockScheduleFacade;

    @Mock
    private HostService mockHostFacade;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void createScheduleShouldCallFacadeMethod() {

        Schedule schedule = new Schedule();

        controller.createSchedule(schedule);

        verify(mockScheduleFacade).saveSchedule(schedule);
    }

    @Test
    public void onSuccessNewScheduleShouldBeReturnedWhenCreated() {

        Schedule newSchedule = new Schedule();
        Schedule schedule = new Schedule();

        when(mockScheduleFacade.saveSchedule(schedule)).thenReturn(newSchedule);

        ResponseEntity<APIResponse> response = controller.createSchedule(schedule);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().body).isEqualTo(newSchedule);
    }

    @Test
    public void updateScheduleShouldCallFacadeWithIdInMethod() {

        Schedule schedule = new Schedule();

        controller.updateSchedule(1L, schedule);

        verify(mockScheduleFacade).saveSchedule(schedule);

        assertThat(schedule.getId()).isEqualTo(1L);
    }

    @Test
    public void onSuccessUpdatedScheduleShouldBeReturnedWhenSaved() {

        Schedule schedule = new Schedule();

        when(mockScheduleFacade.saveSchedule(schedule)).thenReturn(schedule);

        ResponseEntity<APIResponse> response = controller.updateSchedule(1L, schedule);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().body).isEqualTo(schedule);
    }

    @Test
    public void deleteScheduleShouldCallFacade() {

        ResponseEntity<APIResponse> response = controller.deleteSchedule(1L);

        verify(mockScheduleFacade).deleteSchedule(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().body).isNull();
    }

    @Test
    public void createHostShouldCallFacade() {

        Host host = new Host();

        controller.createHost(host);

        verify(mockHostFacade).saveHost(host);
    }

    @Test
    public void saveHostShouldReturnResponse() {

        Host host = new Host();
        Host createdHost = new Host();
        
        when(mockHostFacade.saveHost(host)).thenReturn(createdHost);
        
        ResponseEntity<APIResponse> response = controller.createHost(host);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().body).isEqualTo(createdHost);
    }
    
    @Test
    public void updateHostShouldCallFacadeWithIdInMethod() {

        Host host = new Host();

        controller.updateHost(1L, host);

        verify(mockHostFacade).saveHost(host);

        assertThat(host.getId()).isEqualTo(1L);
    }

    @Test
    public void onSuccessUpdatedHostShouldBeReturnedWhenSaved() {

        Host host = new Host();

        when(mockHostFacade.saveHost(host)).thenReturn(host);

        ResponseEntity<APIResponse> response = controller.updateHost(1L, host);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().body).isEqualTo(host);
    }
    
    @Test
    public void deleteHostShouldCallFacade() {

        ResponseEntity<APIResponse> response = controller.deleteHost(1L);

        verify(mockHostFacade).deleteHost(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().body).isNull();
    }
}
