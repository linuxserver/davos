package io.linuxserver.davos.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.linuxserver.davos.exception.ScheduleAlreadyRunningException;
import io.linuxserver.davos.exception.ScheduleNotRunningException;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.ScheduleModel;

public class ScheduleExecutorTest {

    @InjectMocks
    private ScheduleExecutor scheduleExecutor = new ScheduleExecutor();

    @Mock
    private ScheduleDAO mockConfigurationDAO;

    @Mock
    private ScheduledExecutorService mockExecutorService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldScheduleBasedOnIntervalAndAutoStartup() {

        List<ScheduleModel> models = new ArrayList<ScheduleModel>();

        ScheduleModel nonAutoModel = new ScheduleModel();
        nonAutoModel.startAutomatically = false;

        ScheduleModel autoModel = new ScheduleModel();
        autoModel.startAutomatically = true;
        autoModel.interval = 50;

        models.add(nonAutoModel);
        models.add(autoModel);

        when(mockConfigurationDAO.getAll()).thenReturn(models);

        scheduleExecutor.runAutomaticStartupSchedules();

        verify(mockExecutorService).scheduleAtFixedRate(any(RunnableSchedule.class), eq(0l), eq(50l), eq(TimeUnit.MINUTES));
    }

    @Test
    public void startScheduleShouldRunThatSchedule() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;

        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);

        scheduleExecutor.startSchedule(1337L);

        verify(mockExecutorService).scheduleAtFixedRate(any(RunnableSchedule.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES));
    }

    @Test(expected = ScheduleAlreadyRunningException.class)
    public void startScheduleShouldNotRunScheduleIfAlreadyRunning() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;
        config.id = 1337L;

        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.startSchedule(1337L);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stopScheduleShouldStopRunningSchedule() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;
        config.id = 1337L;

        @SuppressWarnings("rawtypes")
        ScheduledFuture mockFuture = mock(ScheduledFuture.class);

        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);
        when(mockExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES))).thenReturn(mockFuture);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.stopSchedule(1337L);
        
        verify(mockFuture).cancel(true);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldBeAbleToInformWhetherScheduleIsRunningOrNot() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;
        config.id = 1337L;

        @SuppressWarnings("rawtypes")
        ScheduledFuture mockFuture = mock(ScheduledFuture.class);

        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);
        when(mockExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES))).thenReturn(mockFuture);

        scheduleExecutor.startSchedule(1337L);
        assertThat(scheduleExecutor.isScheduleRunning(1337L)).isTrue();
        
        scheduleExecutor.stopSchedule(1337L);
        assertThat(scheduleExecutor.isScheduleRunning(1337L)).isFalse();
        
        verify(mockFuture).cancel(true);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void stopScheduleShouldNotStopRunningScheduleIfItHasAlreadyBeenCancelled() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;
        config.id = 1337L;

        @SuppressWarnings("rawtypes")
        ScheduledFuture mockFuture = mock(ScheduledFuture.class);
        when(mockFuture.isCancelled()).thenReturn(true);
        
        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);
        when(mockExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES))).thenReturn(mockFuture);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.stopSchedule(1337L);
        
        verify(mockFuture, never()).cancel(true);
    }
    
    @Test(expected = ScheduleNotRunningException.class)
    public void stopScheduleShouldNotAttemptToStopNonRunningSchedule() {

        ScheduleModel config = new ScheduleModel();
        config.interval = 86;
        config.id = 1337L;

        when(mockConfigurationDAO.fetchSchedule(1337L)).thenReturn(config);

        scheduleExecutor.stopSchedule(1337L);
    }
}
