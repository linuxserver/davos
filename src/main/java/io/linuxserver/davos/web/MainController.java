package io.linuxserver.davos.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.linuxserver.davos.dto.ScheduleConfigurationDTO;
import io.linuxserver.davos.dto.converters.ScheduleConfigurationDTOConverter;
import io.linuxserver.davos.persistence.dao.ScheduleConfigurationDAO;
import io.linuxserver.davos.persistence.model.ScheduleConfigurationModel;
import io.linuxserver.davos.transfer.ftp.FileTransferType;
import io.linuxserver.davos.transfer.ftp.TransferProtocol;

@Controller
@RequestMapping("/")
public class MainController {

    @Resource
    private ScheduleConfigurationDAO scheduleConfigurationDAO;

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping("/scheduling")
    public String scheduling(Model model) {

        model.addAttribute("schedules", scheduleConfigurationDAO.getAll());

        return "scheduling";
    }

    @RequestMapping("/scheduling/{id}")
    public String schedulingEdit(@PathVariable Long id, Model model) {

        ScheduleConfigurationModel config = scheduleConfigurationDAO.getConfig(id);
        model.addAttribute("schedule", new ScheduleConfigurationDTOConverter().convert(config));

        return "schedulingedit";
    }

    @RequestMapping("/scheduling/new")
    public String schedulingNew(Model model) {

        ScheduleConfigurationDTO schedule = new ScheduleConfigurationDTO();
        schedule.interval = 60;
        schedule.transferType = FileTransferType.FILE;
        schedule.connectionType = TransferProtocol.FTP;
        
        model.addAttribute("schedule", schedule);

        return "schedulingedit";
    }
}
