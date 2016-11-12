package io.linuxserver.davos.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.linuxserver.davos.delegation.facades.HostFacade;
import io.linuxserver.davos.delegation.facades.ScheduleFacade;
import io.linuxserver.davos.dto.HostDTO;
import io.linuxserver.davos.web.ScheduleView;
import io.linuxserver.davos.web.selectors.IntervalSelector;
import io.linuxserver.davos.web.selectors.MethodSelector;
import io.linuxserver.davos.web.selectors.ProtocolSelector;
import io.linuxserver.davos.web.selectors.TransferSelector;

@Controller
@RequestMapping("/v2")
public class ViewV2Controller {

    @Resource
    private ScheduleFacade scheduleFacade;

    @Resource
    private HostFacade hostFacade;

    @ModelAttribute("allIntervals")
    public List<IntervalSelector> populateIntervals() {
        return Arrays.asList(IntervalSelector.ALL);
    }

    @ModelAttribute("allProtocols")
    public List<ProtocolSelector> populateProtocols() {
        return Arrays.asList(ProtocolSelector.ALL);
    }

    @ModelAttribute("allTransferTypes")
    public List<TransferSelector> populateTypes() {
        return Arrays.asList(TransferSelector.ALL);
    }
    
    @ModelAttribute("allMethods")
    public List<MethodSelector> populateMethods() {
        return Arrays.asList(MethodSelector.ALL);
    }

    @ModelAttribute("allHosts")
    public List<HostDTO> allHosts() {
        return hostFacade.fetchAllHosts();
    }

    @RequestMapping
    public String index() {
        return "v2/index";
    }

    @RequestMapping("/settings")
    public String settings() {
        return "v2/settings";
    }

    @RequestMapping("/schedules")
    public String schedules(Model model) {

        model.addAttribute("schedules", scheduleFacade.fetchAllSchedules());

        return "v2/schedules";
    }

    @RequestMapping("/schedules/new")
    public String newSchedule(Model model) {
        
        ScheduleView schedule = new ScheduleView();
       
        model.addAttribute("schedule", schedule);
        return "v2/edit-schedule";
    }

    @RequestMapping("/schedules/{id}")
    public String schedules(@PathVariable Long id, Model model) {

        model.addAttribute("schedule", scheduleFacade.fetchSchedule(id));

        return "v2/edit-schedule";
    }

    @RequestMapping("/hosts")
    public String hosts(Model model) {
        return "v2/hosts";
    }

    @RequestMapping("/hosts/new")
    public String newHost(Model model) {

        model.addAttribute("host", hostFacade.createBlankHost());

        return "v2/edit-host";
    }

    @RequestMapping("/hosts/{id}")
    public String hosts(@PathVariable Long id, Model model) {

        model.addAttribute("host", hostFacade.fetchHost(id));

        return "v2/edit-host";
    }
}
