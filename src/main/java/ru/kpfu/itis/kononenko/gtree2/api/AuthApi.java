package ru.kpfu.itis.kononenko.gtree2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.aop.RateLimit;
import ru.kpfu.itis.kononenko.gtree2.dto.request.RefreshRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;

@Tag(name = "Auth")
@RequestMapping("/auth")
public interface AuthApi {
    @Operation(summary = "Auth sign in page")
    @GetMapping("/sign-in")
    String signInGet();

    @Operation(summary = "Registration page")
    @GetMapping("/sign-up")
    String signUpGet();

    @Operation(summary = "Sign out user")
    @GetMapping("/sign-out")
    String signOutGet();

    @Operation(summary = "Confirm email")
    @GetMapping("/confirm")
    String confirmAccount(@RequestParam("token") String token);

    @Operation(summary = "Forgot password page")
    @GetMapping("/forgot-password")
    String forgotPasswordGet();

    @Operation(summary = "Reset password page")
    @GetMapping("/reset-password")
    String resetPasswordGet();

    @Operation(summary = "Confirm password reset")
    @GetMapping("/reset-password-confirm")
    String resetPasswordConfirmGet();

    @Operation(summary = "Process sign in")
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

    @Operation(summary = "Process sign up")
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> signUpPost(
            @RequestBody
            @Valid
            UserRegisterRequest request
    );


    @Operation(summary = "Refresh access token")
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> refreshToken(
            @RequestBody
            @Valid
            RefreshRequest request,
            HttpServletResponse response
    );

}
