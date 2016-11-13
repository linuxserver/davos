package io.linuxserver.davos.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.linuxserver.davos.persistence.model.ScannedFileModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
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

    @Test
    public void updatingScannedFilesShouldWork() {
        
        ScheduleModel model = new ScheduleModel();
        model.id = 1L;
        model.scannedFiles.add(toScannedFileModel("oldFile", model));
        model.scannedFiles.add(toScannedFileModel("another", model));
        model.scannedFiles.add(toScannedFileModel("blah", model));
        
        List<String> files = Arrays.asList("file1", "file2");
        
        when(mockRepository.findOne(1L)).thenReturn(model);
        
        configDAO.updateScannedFilesOnSchedule(1L, files);
        
        assertThat(model.scannedFiles).hasSize(2);
        assertThat(model.scannedFiles.get(0).file).isEqualTo("file1");
        assertThat(model.scannedFiles.get(0).schedule).isEqualTo(model);
        
        assertThat(model.scannedFiles.get(1).file).isEqualTo("file2");
        assertThat(model.scannedFiles.get(1).schedule).isEqualTo(model);
    }

    private ScannedFileModel toScannedFileModel(String fileName, ScheduleModel model) {

        ScannedFileModel scannedFileModel = new ScannedFileModel();
        scannedFileModel.file = fileName;
        scannedFileModel.schedule = model;

        return scannedFileModel;
    }
}
