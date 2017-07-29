package io.linuxserver.davos.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.linuxserver.davos.delegation.services.ScheduleService;
import io.linuxserver.davos.web.selectors.MethodSelector;

@Controller
@RequestMapping("/fragments")
public class FragmentController {

    @Resource
    private ScheduleService scheduleService;
    
    @ModelAttribute("allMethods")
    public List<MethodSelector> populateMethods() {
        return Arrays.asList(MethodSelector.ALL);
    }
    
    @RequestMapping("/filter")
    public String filter(@RequestParam("value") String value, Model model) {
        
        model.addAttribute("value", value);
        
        return "fragments/filter";
    }
    
    @RequestMapping("/notification/pushbullet")
    public String notificationPushbullet() {
        return "fragments/pushbullet";
    }
    
    @RequestMapping("/notification/sns")
    public String notificationSns() {
        return "fragments/sns";
    }
    
    @RequestMapping("/api")
    public String api() {
        return "fragments/api";
    }
    
    @RequestMapping("/schedule/{id}/transfers")
    public String transfers(@PathVariable Long id, Model model) {
        
        model.addAttribute("schedule", scheduleService.fetchSchedule(id));
        
        return "fragments/transfers";
    }
}
