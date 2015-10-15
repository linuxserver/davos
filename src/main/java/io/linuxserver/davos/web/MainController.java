package io.linuxserver.davos.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.linuxserver.davos.persistence.repository.ScheduleConfigurationRepository;

@Controller
public class MainController {

    @Resource
    private ScheduleConfigurationRepository scheduleConfigurationRepository;
    
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping("/scheduling")
    public String scheduling(Model model) {
        
        model.addAttribute("schedules", scheduleConfigurationRepository.findAll());
        
        return "scheduling";
    }
    
    @RequestMapping("/scheduling/edit")
    public String schedulingEdit() {
        return "schedulingedit";
    }
}
