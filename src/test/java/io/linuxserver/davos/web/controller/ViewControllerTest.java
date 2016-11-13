package io.linuxserver.davos.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.Model;

import io.linuxserver.davos.delegation.services.HostService;
import io.linuxserver.davos.delegation.services.ScheduleService;
import io.linuxserver.davos.delegation.services.SettingsService;
import io.linuxserver.davos.web.Host;
import io.linuxserver.davos.web.Schedule;
import io.linuxserver.davos.web.selectors.LogLevelSelector;

public class ViewControllerTest {

    @InjectMocks
    private ViewController controller = new ViewController();
    
    @Mock
    private HostService mockHostFacade;
    
    @Mock
    private ScheduleService mockScheduleFacade;
    
    @Mock
    private SettingsService mockSettingsService;
    
    @Mock
    private Model mockModel;
    
    @Before
    public void before() {
        initMocks(this);
        
        when(mockSettingsService.getCurrentLoggingLevel()).thenReturn(LogLevelSelector.DEBUG);
    }
    
    @Test
    public void viewsShouldResolveCorrectly() {
        
        assertThat(controller.index()).isEqualTo("v2/index");
        assertThat(controller.settings(mockModel)).isEqualTo("v2/settings");
        assertThat(controller.schedules(mockModel)).isEqualTo("v2/schedules");
        assertThat(controller.schedules(1L, mockModel)).isEqualTo("v2/edit-schedule");
        assertThat(controller.newSchedule(mockModel)).isEqualTo("v2/edit-schedule");
        assertThat(controller.hosts()).isEqualTo("v2/hosts");
        assertThat(controller.newHost(mockModel)).isEqualTo("v2/edit-host");
        assertThat(controller.hosts(1L, mockModel)).isEqualTo("v2/edit-host");
    }
    
    @Test 
    public void schedulesShouldAddAllSchedulesToModel() {
        
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(new Schedule());
        
        when(mockScheduleFacade.fetchAllSchedules()).thenReturn(schedules);
        
        controller.schedules(mockModel);
        
        verify(mockModel).addAttribute("schedules", schedules);
    }
    
    @Test 
    public void schedulesWithIdShouldAddSpecificScheduleToModel() {
        
        Schedule schedule = new Schedule();
        
        when(mockScheduleFacade.fetchSchedule(1L)).thenReturn(schedule);
        
        controller.schedules(1L, mockModel);
        
        verify(mockModel).addAttribute("schedule", schedule);
    }
    
    @Test 
    public void newScheduleShouldAddScheduleToModel() {
        
        controller.newSchedule(mockModel);
        
        verify(mockModel).addAttribute(eq("schedule"), any(Schedule.class));
    }
    
    @Test
    public void allHostsShouldAddHostsToModel() {
        
        controller.allHosts();
        
        verify(mockHostFacade).fetchAllHosts();
    }
    
    @Test 
    public void newHostShouldAddNewHostToModel() {
        
        controller.newHost(mockModel);
        
        verify(mockModel).addAttribute(eq("host"), any(Host.class));
    }
    
    @Test
    public void hostsWithIdShouldAddSpecificHostToModel() {
        
        Host host = new Host();
        when(mockHostFacade.fetchHost(1L)).thenReturn(host);
        
        controller.hosts(1L, mockModel);
        
        verify(mockModel).addAttribute("host", host);
    }
}
