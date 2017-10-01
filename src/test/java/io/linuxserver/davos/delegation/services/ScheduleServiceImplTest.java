package io.linuxserver.davos.delegation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import io.linuxserver.davos.converters.ScheduleConverter;
import io.linuxserver.davos.persistence.dao.HostDAO;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.model.ScannedFileModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.ScheduleExecutor;
import io.linuxserver.davos.web.Schedule;

public class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleService scheduleService = new ScheduleServiceImpl();
        
    @Mock
    private ScheduleDAO mockScheduleDAO;
    
    @Spy
    private ScheduleConverter scheduleConverter;
    
    @Mock
    private ScheduleExecutor mockExecutor;
    
    @Mock
    private HostDAO mockHostDAO;
    
    @Captor
    public ArgumentCaptor<ScheduleModel> scheduleCaptor;
    
    @Before
    public void before() {
        initMocks(this);
    }
    
    @Test
    public void shouldStartScheduleFromExecutor() {
        
        scheduleService.startSchedule(1L);
        verify(mockExecutor).startSchedule(1L);
    }
    
    @Test
    public void shouldStopScheduleFromExecutor() {
        
        scheduleService.stopSchedule(1L);
        verify(mockExecutor).stopSchedule(1L);
    }
    
    @Test
    public void shouldDeleteScheduleWhenNotRunning() {
        
        scheduleService.deleteSchedule(1L);
        
        verify(mockExecutor, never()).stopSchedule(1L);
        verify(mockScheduleDAO).deleteSchedule(1L);
    }
    
    @Test
    public void shouldCheckIfScheduleIsRunningAndStopIfSoBeforeDeleting() {
        
        when(mockExecutor.isScheduleRunning(1L)).thenReturn(true);
        
        scheduleService.deleteSchedule(1L);
        
        verify(mockExecutor).stopSchedule(1L);
        verify(mockScheduleDAO).deleteSchedule(1L);
    }
    
    @Test
    public void shouldGetAllSchedulesAndConvert() {
        
        List<ScheduleModel> models = new ArrayList<ScheduleModel>();
        
        ScheduleModel model1 = new ScheduleModel();
        model1.id = 1L;
        model1.name = "Test 1";
        model1.host = new HostModel();
        
        ScheduleModel model2 = new ScheduleModel();
        model2.id = 2L;
        model2.name = "Test 2";
        model2.host = new HostModel();
        
        models.add(model1);
        models.add(model2);
        
        when(mockScheduleDAO.getAll()).thenReturn(models);
        
        List<Schedule> schedules = scheduleService.fetchAllSchedules();
        
        assertThat(schedules).hasSize(2);
        
        assertThat(schedules.get(0).getId()).isEqualTo(1L);
        assertThat(schedules.get(0).getName()).isEqualTo("Test 1");
          
        assertThat(schedules.get(1).getId()).isEqualTo(2L);
        assertThat(schedules.get(1).getName()).isEqualTo("Test 2");
    }
    
    @Test
    public void shouldReturnOneSchedule() {
        
        ScheduleModel model1 = new ScheduleModel();
        model1.id = 1L;
        model1.name = "Test 1";
        model1.host = new HostModel();
        
        when(mockScheduleDAO.fetchSchedule(1L)).thenReturn(model1);        
        
        Schedule schedule = scheduleService.fetchSchedule(1L);
        
        assertThat(schedule.getId()).isEqualTo(1L);
        assertThat(schedule.getName()).isEqualTo("Test 1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldGetHostFromDatabaseToCheckItExistsWhenCreating() {
        
        Schedule schedule = new Schedule();
        schedule.setHost(null);
        
        scheduleService.createSchedule(schedule);
    }
    
    @Test
    public void shouldOverlayHostFromDatabaseInScheduleWhenCreating() {
        
        ScheduleModel model1 = new ScheduleModel();
        model1.id = 1L;
        model1.name = "Test 1";
        model1.host = new HostModel();
        
        when(mockScheduleDAO.updateConfig(any(ScheduleModel.class))).thenReturn(model1);
        
        HostModel hostModell = new HostModel();
        when(mockHostDAO.fetchHost(2L)).thenReturn(hostModell);
        
        Schedule schedule = new Schedule();
        schedule.setHost(2L);
        scheduleService.createSchedule(schedule);
        
        verify(mockScheduleDAO).updateConfig(scheduleCaptor.capture());
        
        assertThat(scheduleCaptor.getValue().host).isEqualTo(hostModell);
    }
    
    @Test
    public void shouldReturnConvertedScheduleOnceCreated() {
        
        ScheduleModel model1 = new ScheduleModel();
        model1.id = 1L;
        model1.name = "Test 1";
        model1.host = new HostModel();
        
        when(mockScheduleDAO.updateConfig(any(ScheduleModel.class))).thenReturn(model1);
        
        HostModel hostModell = new HostModel();
        when(mockHostDAO.fetchHost(2L)).thenReturn(hostModell);
        
        Schedule schedule = new Schedule();
        schedule.setHost(2L);
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
                
        assertThat(createdSchedule.getId()).isEqualTo(1L);
        assertThat(createdSchedule.getName()).isEqualTo("Test 1");
    }
    
    @Test
    public void shouldOverlayHostFromDatabaseInScheduleWhenUpdating() {
        
        setUpScheduleMocks();
        
        HostModel hostModel1 = new HostModel();
        when(mockHostDAO.fetchHost(2L)).thenReturn(hostModel1);
        
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setHost(2L);
        scheduleService.updateSchedule(schedule);
        
        verify(mockScheduleDAO).updateConfig(scheduleCaptor.capture());
        
        assertThat(scheduleCaptor.getValue().host).isEqualTo(hostModel1);
    }
    
    @Test
    public void shouldReturnConvertedScheduleOnceUpdated() {
        
        setUpScheduleMocks();
        
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setHost(2L);
        Schedule createdSchedule = scheduleService.updateSchedule(schedule);
                
        assertThat(createdSchedule.getId()).isEqualTo(1L);
        assertThat(createdSchedule.getName()).isEqualTo("Test 1");
    }
    
    @Test
    public void shouldOverlayLastRunTimeOfExistingScheduleToNewOne() {
        
        setUpScheduleMocks();
        
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setHost(2L);
        scheduleService.updateSchedule(schedule);
        
        verify(mockScheduleDAO).updateConfig(scheduleCaptor.capture());
        
        assertThat(scheduleCaptor.getValue().getLastRunTime()).isEqualTo(12345L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfScheduleHasNoIdWhenUpdating() {
        
        Schedule schedule = new Schedule();
        schedule.setHost(2L);
        
        HostModel hostModell = new HostModel();
        when(mockHostDAO.fetchHost(2L)).thenReturn(hostModell);
        
        scheduleService.updateSchedule(schedule);
    }
    
    @Test
    public void shouldClearScannedFiles() {
        
        ScheduleModel model = new ScheduleModel();
        model.scannedFiles = new ArrayList<ScannedFileModel>();
        model.scannedFiles.add(new ScannedFileModel());
        
        when(mockScheduleDAO.fetchSchedule(1L)).thenReturn(model);
        
        assertThat(model.scannedFiles).hasSize(1);
        
        scheduleService.clearScannedFilesFromSchedule(1L);
        
        assertThat(model.scannedFiles).isEmpty();
        verify(mockScheduleDAO).updateConfig(model);
    }
    
    private void setUpScheduleMocks() {
        
        ScheduleModel model1 = new ScheduleModel();
        model1.id = 1L;
        model1.name = "Test 1";
        model1.setLastRunTime(12345L);
        model1.host = new HostModel();
        
        when(mockScheduleDAO.updateConfig(any(ScheduleModel.class))).thenReturn(model1);
        when(mockScheduleDAO.fetchSchedule(1L)).thenReturn(model1);
        
        when(mockHostDAO.fetchHost(2L)).thenReturn(new HostModel());
    }
}
