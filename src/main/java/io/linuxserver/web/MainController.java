package io.linuxserver.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping("/scheduling")
    public String scheduling() {
        return "scheduling";
    }
    
    @RequestMapping("/scheduling/edit")
    public String schedulingEdit() {
        return "schedulingedit";
    }
}
