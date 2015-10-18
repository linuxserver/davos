package io.linuxserver.davos.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.persistence.repository.ScheduleConfigurationRepository;

public class DefaultScheduleConfigurationDAOTest {

    @Mock
    private ScheduleConfigurationRepository mockRepository;
    
    @InjectMocks
    private DefaultScheduleConfigurationDAO configDAO = new DefaultScheduleConfigurationDAO();
    
    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @Test
    public void updateLastRunShouldOnlySetLastRunOnExisting() {

        ArgumentCaptor<ScheduleConfigurationModel> captor = ArgumentCaptor.forClass(ScheduleConfigurationModel.class);
        
        ScheduleConfigurationModel model = new ScheduleConfigurationModel();
        model.lastRun = new Date();
        model.interval = 3;
        
        when(mockRepository.findOne(1L)).thenReturn(model);
        
        configDAO.updateLastRun(1L, new DateTime(2015, 1, 2, 0, 0));
        
        verify(mockRepository).save(captor.capture());
        
        assertThat(captor.getValue().lastRun).isEqualTo(new DateTime(2015, 1, 2, 0, 0).toDate());
        assertThat(captor.getValue().interval).isEqualTo(3);
    }
}
