package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.kononenko.gtree2.config.property.GoogleOAuthProperties;
import ru.kpfu.itis.kononenko.gtree2.dto.GoogleUserInfo;
import ru.kpfu.itis.kononenko.gtree2.entity.ERole;
import ru.kpfu.itis.kononenko.gtree2.entity.Role;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.repository.RoleRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.UserRepository;
import ru.kpfu.itis.kononenko.gtree2.service.impl.GoogleOAuthService;
import ru.kpfu.itis.kononenko.gtree2.service.impl.RefreshTokenService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetailsService;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final GoogleOAuthProperties properties;
    private final GoogleOAuthService oauthService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + URLEncoder.encode(properties.getClientId(), StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(properties.getRedirectUri(), StandardCharsets.UTF_8) +
                "&response_type=code&scope=openid%20email%20profile&access_type=offline";
        response.sendRedirect(url);
    }

    @GetMapping("/google/callback")
    public String handleGoogleCallback(@RequestParam("code") String code,
                                       HttpServletResponse response) throws IOException {
        GoogleUserInfo info = oauthService.fetchUser(code);

        Optional<User> optionalUser = userRepository.findByEmail(info.getEmail());
        User user = optionalUser.orElseGet(() -> {
            User u = new User();
            String baseUsername = info.getEmail().split("@")[0];
            String username = baseUsername;
            int i = 1;
            while (userRepository.existsByUsername(username)) {
                username = baseUsername + i++;
            }
            u.setUsername(username);
            u.setEmail(info.getEmail());
            u.setPasswordHash(UUID.randomUUID().toString());
            u.setEmailVerified(true);
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow();
            u.getRoles().add(role);
            return userRepository.save(u);
        });

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        refreshTokenService.save(userDetails.getUsername(), refreshToken);

        AuthController.setCookie(response, accessToken, refreshToken);
        return "redirect:/profile/me";
    }
}