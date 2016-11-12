package io.linuxserver.davos.delegation.services;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import io.linuxserver.davos.converters.ScheduleConverter;
import io.linuxserver.davos.persistence.dao.HostDAO;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;

public class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleService scheduleService = new ScheduleServiceImpl();
    
    @Spy
    private ScheduleConverter mockScheduleConverter;
    
    @Mock
    private ScheduleDAO scheduleDAO;
    
    @Mock
    private HostDAO mockHostDAO;
    
    @Before
    public void before() {
        initMocks(this);
    }
}
