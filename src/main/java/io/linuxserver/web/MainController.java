package io.linuxserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

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
