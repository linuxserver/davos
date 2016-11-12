package io.linuxserver.davos.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.linuxserver.davos.web.selectors.MethodSelector;

@Controller
@RequestMapping("/fragments")
public class FragmentController {

    @ModelAttribute("allMethods")
    public List<MethodSelector> populateMethods() {
        return Arrays.asList(MethodSelector.ALL);
    }
    
    @RequestMapping("/filter")
    public String filter(@RequestParam("value") String value, Model model) {
        
        model.addAttribute("value", value);
        
        return "fragments/filter";
    }
    
    @RequestMapping("/notification")
    public String notification() {
        return "fragments/notification";
    }
    
    @RequestMapping("/api")
    public String api() {
        return "fragments/api";
    }
}
