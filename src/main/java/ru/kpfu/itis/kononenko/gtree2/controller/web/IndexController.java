package ru.kpfu.itis.kononenko.gtree2.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/faq")
    public String faq() {
        return "faq";
    }

}
