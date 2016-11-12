package io.linuxserver.davos.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.linuxserver.davos.bdd.helpers.FakeFTPServerFactory;
import io.linuxserver.davos.persistence.dao.ScheduleDAO;
import io.linuxserver.davos.persistence.model.FilterModel;
import io.linuxserver.davos.persistence.model.HostModel;
import io.linuxserver.davos.persistence.model.ScheduleModel;
import io.linuxserver.davos.schedule.RunnableSchedule;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

public class ScheduleStepDefs {

    private static final String TMP = FileUtils.getTempDirectoryPath();
    
    private ScheduleModel scheduleModel;
    
    @Given("^a schedule exists for that server, with filters$")
    public void a_schedule_exists_for_that_server_with_filters() throws Throwable {
        
        scheduleModel = new ScheduleModel();
        scheduleModel.host = new HostModel();
        
        scheduleModel.host.address = "localhost";
        scheduleModel.host.port = FakeFTPServerFactory.getPort();
        scheduleModel.host.username = "user";
        scheduleModel.host.password = "password";
        scheduleModel.host.protocol = TransferProtocol.FTP;
        scheduleModel.remoteFilePath = "/tmp";
        scheduleModel.localFilePath = TMP;
        
        FilterModel filter1 = new FilterModel();
        filter1.value = "file2*";
        scheduleModel.filters.add(filter1);
        
        FilterModel filter2 = new FilterModel();
        filter2.value = "file3*";
        scheduleModel.filters.add(filter2);
    }

    @When("^that schedule is run$")
    public void that_schedule_is_run() throws Throwable {
        new RunnableSchedule(1L, new CucumberScheduleConfigurationDAO()).run();
    }

    @Then("^only the filtered files are downloaded$")
    public void only_the_filtered_files_are_downloaded() throws Throwable {
        
        File file1 = new File(TMP + "/file1.txt");
        File file2 = new File(TMP + "/file2.txt");
        File file3 = new File(TMP + "/file3.txt");
        
        assertThat(file1.exists()).isFalse();
        assertThat(file2.exists()).isTrue();
        assertThat(file3.exists()).isTrue();
        
        file2.delete();
        file3.delete();
    }
    
    class CucumberScheduleConfigurationDAO implements ScheduleDAO {

        @Override
        public List<ScheduleModel> getAll() {
            return null;
        }

        @Override
        public ScheduleModel fetchSchedule(Long id) {
            return scheduleModel;
        }

        @Override
        public ScheduleModel updateConfig(ScheduleModel model) {
            return null;
        }
    }
}
