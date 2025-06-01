package ru.kpfu.itis.kononenko.gtree2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/me")
    public String myProfile() {
        return "profile-me";
    }

    @GetMapping("/{username}")
    public String foreignProfile() {
        return "profile-all";
    }
}
