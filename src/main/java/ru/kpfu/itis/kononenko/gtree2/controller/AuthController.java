package ru.kpfu.itis.kononenko.gtree2.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kpfu.itis.kononenko.gtree2.api.AuthApi;
import ru.kpfu.itis.kononenko.gtree2.dto.request.RefreshRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserRegisterRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.entity.VerificationToken;
import ru.kpfu.itis.kononenko.gtree2.enums.TokenStatus;
import ru.kpfu.itis.kononenko.gtree2.exception.ExpiredTokenException;
import ru.kpfu.itis.kononenko.gtree2.service.impl.MailService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.RefreshTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.UserService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.VerificationTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetailsService;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtTokenProvider;

import java.time.Duration;
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
    private final CustomUserDetailsService userDetailsService;


    @GetMapping("/sing-success")
    public String signSuccessPage() {
        return "sing-success";
    }


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
        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attr.getRequest();
        HttpServletResponse response = attr.getResponse();

        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                }
            }
        }

        if (refresh != null) {
            try {
                String username = jwtTokenProvider.getUsernameFromToken(refresh);
                refreshTokenService.invalidate(username);
            } catch (Exception ignored) {
            }
        }

        clearCookie(response, "accessToken");
        clearCookie(response, "refreshToken");

        return "redirect:/";
    }

    private static void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    @Override
    public ResponseEntity<?> singInPost(UserLoginRequest request, HttpServletResponse response) {

        CustomUserDetails userDetails = (CustomUserDetails) authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())).getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        refreshTokenService.save(userDetails.getUsername(), refreshToken);

        setCookie(response, accessToken, refreshToken);

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

        User user = userService.save(request);
        VerificationToken token = tokenService.createToken(user);
        mailService.sendVerificationEmail(user, token);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "redirectUrl", "/auth/sign-in"
                )
        );
    }



    public String confirmAccount(String token) {
        TokenStatus status = tokenService.verifyToken(token);

//        return switch (status) {
//            case VALID -> ResponseEntity.ok("Email подтвержден! Аккаунт активирован.");
//            case EXPIRED -> ResponseEntity.badRequest().body("Срок действия токена истек. Запросите новый.");
//            case ALREADY_USED -> ResponseEntity.badRequest().body("Токен уже был использован ранее.");
//            default -> ResponseEntity.badRequest().body("Неверный токен подтверждения!");
//        };
        if (status == TokenStatus.VALID) {
            return "verification_success";
        }
        return "verification_result";
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshRequest request,
                                          HttpServletResponse response) {

        String refreshToken = request.refreshToken();

        try {
            if (!jwtTokenProvider.isValid(refreshToken)) {
                return ResponseEntity.status(401).build();
            }
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        if (!refreshTokenService.isStored(refreshToken, username)) {
            return ResponseEntity.status(401).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        String newAccess = jwtTokenProvider.generateAccessToken(userDetails);
        String newRefresh = jwtTokenProvider.generateRefreshToken(userDetails);
        refreshTokenService.save(username, newRefresh);

        setCookie(response, newAccess, newRefresh);

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", newAccess,
                        "refreshToken", newRefresh
                )
        );
    }

    public static void setCookie(HttpServletResponse response, String newAccess, String newRefresh) {
        Cookie accessCookie = new Cookie("accessToken", newAccess);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) Duration.ofHours(24).getSeconds());

        Cookie refreshCookie = new Cookie("refreshToken", newRefresh);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) Duration.ofDays(7).getSeconds());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
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
