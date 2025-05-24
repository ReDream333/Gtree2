package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.aop.RateLimit;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.entity.VerificationToken;
import ru.kpfu.itis.kononenko.gtree2.enums.TokenStatus;
import ru.kpfu.itis.kononenko.gtree2.service.MailService;
import ru.kpfu.itis.kononenko.gtree2.service.UserService;
import ru.kpfu.itis.kononenko.gtree2.service.VerificationTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtService;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final MailService mailService;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    @GetMapping("/sign-in")
    public String signIn() {
        return "/views/sign-in";
    }

    @RateLimit(permits = 10, duration = 1, unit = TimeUnit.MINUTES)
    @PostMapping(value = "/sign-in", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> login(@RequestBody UserLoginRequest dto) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        String token = jwt.generate((UserDetails) auth.getPrincipal());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "redirectUrl", "/profile"
                )
        );
    }

    @RateLimit(permits = 10, duration = 1, unit = TimeUnit.MINUTES)
    @GetMapping("/sign-up")
    public String signUpGet(Model model) {
//        model.addAttribute("signUpForm", new UserRegisterRequest(null, null, null));
        return "registration";
    }

    @PostMapping("/sign-up")
    public String signUpPost(
//            @ModelAttribute("signUpForm")
            @Validated UserRegisterRequest userRegisterRequest,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userService.findByUsername(userRegisterRequest.username()).isPresent()) {
            bindingResult.rejectValue("username", "error.usernameExists", "User already exists");
            return "registration";
        }

        if (userService.findByEmail(userRegisterRequest.email()).isPresent()) {
            bindingResult.rejectValue("email", "error.email", "Email is already in use");
            return "registration";
        }

        User newUser = userService.save(userRegisterRequest);
        VerificationToken token = tokenService.createToken(newUser);
        mailService.sendVerificationEmail(newUser, token);

        model.addAttribute("email", newUser.getEmail());
        return "auth/confirmation-sent";
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        TokenStatus status = tokenService.verifyToken(token);
        return switch (status) {
            case VALID -> ResponseEntity.ok("Email подтвержден! Аккаунт активирован.");
            case EXPIRED -> ResponseEntity.badRequest().body("Срок действия токена истек. Запросите новый.");
            case ALREADY_USED -> ResponseEntity.badRequest().body("Токен уже был использован ранее.");
            default -> ResponseEntity.badRequest().body("Неверный токен подтверждения!");
        };
    }
}
