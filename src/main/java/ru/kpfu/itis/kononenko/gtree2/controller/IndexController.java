package ru.kpfu.itis.kononenko.gtree2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/index")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/profile")
    public String profile() {
        return "profile";
    }
}
