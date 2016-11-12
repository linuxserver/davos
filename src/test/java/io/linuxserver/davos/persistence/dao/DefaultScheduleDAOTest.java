package io.linuxserver.davos.persistence.dao;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.linuxserver.davos.persistence.repository.ScheduleRepository;

public class DefaultScheduleDAOTest {

    @Mock
    private ScheduleRepository mockRepository;
    
    @InjectMocks
    private DefaultScheduleDAO configDAO = new DefaultScheduleDAO();
    
    @Before
    public void setUp() {
        initMocks(this);
    }
}
