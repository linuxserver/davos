package io.linuxserver.davos.schedule;

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
import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;

public class ScheduleExecutorTest {

    @InjectMocks
    private ScheduleExecutor scheduleExecutor = new ScheduleExecutor();

    @Mock
    private ScheduleConfigurationDAO mockConfigurationDAO;

    @Mock
    private ScheduledExecutorService mockExecutorService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldScheduleBasedOnIntervalAndAutoStartup() {

        List<ScheduleConfigurationModel> models = new ArrayList<ScheduleConfigurationModel>();

        ScheduleConfigurationModel nonAutoModel = new ScheduleConfigurationModel();
        nonAutoModel.startAutomatically = false;

        ScheduleConfigurationModel autoModel = new ScheduleConfigurationModel();
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

        ScheduleConfigurationModel config = new ScheduleConfigurationModel();
        config.interval = 86;

        when(mockConfigurationDAO.getConfig(1337L)).thenReturn(config);

        scheduleExecutor.startSchedule(1337L);

        verify(mockExecutorService).scheduleAtFixedRate(any(RunnableSchedule.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES));
    }

    @Test(expected = ScheduleAlreadyRunningException.class)
    public void startScheduleShouldNotRunScheduleIfAlreadyRunning() {

        ScheduleConfigurationModel config = new ScheduleConfigurationModel();
        config.interval = 86;
        config.id = 1337L;

        when(mockConfigurationDAO.getConfig(1337L)).thenReturn(config);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.startSchedule(1337L);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stopScheduleShouldStopRunningSchedule() {

        ScheduleConfigurationModel config = new ScheduleConfigurationModel();
        config.interval = 86;
        config.id = 1337L;

        @SuppressWarnings("rawtypes")
        ScheduledFuture mockFuture = mock(ScheduledFuture.class);

        when(mockConfigurationDAO.getConfig(1337L)).thenReturn(config);
        when(mockExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES))).thenReturn(mockFuture);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.stopSchedule(1337L);
        
        verify(mockFuture).cancel(true);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void stopScheduleShouldNotStopRunningScheduleIfItHasAlreadyBeenCancelled() {

        ScheduleConfigurationModel config = new ScheduleConfigurationModel();
        config.interval = 86;
        config.id = 1337L;

        @SuppressWarnings("rawtypes")
        ScheduledFuture mockFuture = mock(ScheduledFuture.class);
        when(mockFuture.isCancelled()).thenReturn(true);
        
        when(mockConfigurationDAO.getConfig(1337L)).thenReturn(config);
        when(mockExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(0l), eq(86l), eq(TimeUnit.MINUTES))).thenReturn(mockFuture);

        scheduleExecutor.startSchedule(1337L);
        scheduleExecutor.stopSchedule(1337L);
        
        verify(mockFuture, never()).cancel(true);
    }
    
    @Test(expected = ScheduleNotRunningException.class)
    public void stopScheduleShouldNotAttemptToStopNonRunningSchedule() {

        ScheduleConfigurationModel config = new ScheduleConfigurationModel();
        config.interval = 86;
        config.id = 1337L;

        when(mockConfigurationDAO.getConfig(1337L)).thenReturn(config);

        scheduleExecutor.stopSchedule(1337L);
    }
}
