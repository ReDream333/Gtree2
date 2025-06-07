package ru.kpfu.itis.kononenko.gtree2.api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.aop.RateLimit;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;

import java.util.concurrent.TimeUnit;

@RequestMapping("/auth")
public interface AuthApi {
    @GetMapping("/sign-in")
    String signInGet();

    @GetMapping("/sign-up")
    String signUpGet();

    @GetMapping("/sign-out")
    String signOutGet();

    @GetMapping("/refresh-token")
    String refreshTokenGet();

    @GetMapping("/confirm")
    ResponseEntity<String> confirmAccount(@RequestParam("token") String token);

    @GetMapping("/forgot-password")
    String forgotPasswordGet();

    @GetMapping("/reset-password")
    String resetPasswordGet();

    @GetMapping("/reset-password-confirm")
    String resetPasswordConfirmGet();

    @RateLimit(permits = 10, duration = 1)
    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> singInPost(
            @RequestBody
            @Valid
            UserLoginRequest request,
            HttpServletResponse response
    );

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> signUpPost(
            @RequestBody
            @Valid
            UserRegisterRequest request
    );

}
