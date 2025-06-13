package ru.kpfu.itis.kononenko.gtree2.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.entity.VerificationToken;
import ru.kpfu.itis.kononenko.gtree2.service.impl.MailService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.VerificationTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final VerificationTokenService tokenService;
    private final MailService mailService;



    @GetMapping("/me")
    public String myProfile() {
        return "profile-me";
    }

    @GetMapping("/{username}")
    public String foreignProfile() {
        return "profile-all";
    }

    @GetMapping("/settings")
    public String settings() {
        return "update";
    }

    @PostMapping("/verify")
    public ResponseEntity<?> sendVerification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        VerificationToken token = tokenService.createToken(user);
        mailService.sendVerificationEmail(user, token);
        return ResponseEntity.ok().build();
    }
}
