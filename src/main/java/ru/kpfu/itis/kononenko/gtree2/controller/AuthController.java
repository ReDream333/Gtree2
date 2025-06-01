package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import ru.kpfu.itis.kononenko.gtree2.api.AuthApi;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.enums.TokenStatus;
import ru.kpfu.itis.kononenko.gtree2.service.impl.MailService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.RefreshTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.UserService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.VerificationTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtTokenProvider;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String signInGet() {
        return "sign-in";
    }

    @Override
    public String signUpGet() {
        return "sign-up";
    }

    @Override
    public String signOutGet() {
        return "";
    }

    @Override
    public String refreshTokenGet() {
        return "";
    }


    @Override
    public ResponseEntity<?> singInPost(UserLoginRequest request) {

        CustomUserDetails userDetails = (CustomUserDetails) authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())).getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        refreshTokenService.save(userDetails.getUsername(), refreshToken);

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken,
                        "redirectUrl", "/profile/me"
                )
        );
    }

    @Override
    public ResponseEntity<?> signUpPost(UserRegisterRequest request) {

        userService.save(request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "redirectUrl", "/"
                )
        );
    }



    public ResponseEntity<String> confirmAccount(String token) {
        TokenStatus status = tokenService.verifyToken(token);
        return switch (status) {
            case VALID -> ResponseEntity.ok("Email подтвержден! Аккаунт активирован.");
            case EXPIRED -> ResponseEntity.badRequest().body("Срок действия токена истек. Запросите новый.");
            case ALREADY_USED -> ResponseEntity.badRequest().body("Токен уже был использован ранее.");
            default -> ResponseEntity.badRequest().body("Неверный токен подтверждения!");
        };
    }

    @Override
    public String forgotPasswordGet() {
        return "";
    }

    @Override
    public String resetPasswordGet() {
        return "";
    }

    @Override
    public String resetPasswordConfirmGet() {
        return "";
    }
}
