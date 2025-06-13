package ru.kpfu.itis.kononenko.gtree2.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.kononenko.gtree2.controller.AuthController;
import ru.kpfu.itis.kononenko.gtree2.exception.ExpiredTokenException;
import ru.kpfu.itis.kononenko.gtree2.service.impl.RefreshTokenService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final RedirectEntryPoint redirectEntryPoint;
    private final RefreshTokenService refreshTokenService;

    private static final List<String> EXCLUDE_URLS = List.of(
            "/css/", "/js/", "/images/", "/auth/", "/error/"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {



        try {
            String token = extractFromCookie(request);

            if (token == null) {
                token = extractFromHeader(request);
            }

            if (token != null) {
                try {
                    if (jwtTokenProvider.isValid(token)) {
                        authenticate(token);
                    }
                } catch (ExpiredTokenException ex) {
                    if (!tryRefresh(request, response)) {
                        response.sendRedirect("/auth/sign-in");
                        return;
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredTokenException | BadCredentialsException ex) {
            SecurityContextHolder.clearContext();
            redirectEntryPoint.commence(request, response, ex);
        }

    }

    private String extractFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                String value = cookie.getValue();
                if (StringUtils.hasText(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    private String extractFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private void authenticate(String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);

        if (username != null && roles != null) {
            try {
                CustomUserDetails userDetails =
                        (CustomUserDetails) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException ex) {
                throw new BadCredentialsException("Invalid JWT token", ex);
            }
        }
    }

    private boolean tryRefresh(HttpServletRequest request, HttpServletResponse response) {
        String refresh = extractRefreshFromCookie(request);
        if (refresh == null) {
            return false;
        }

        try {
            if (!jwtTokenProvider.isValid(refresh)) {
                return false;
            }
        } catch (ExpiredTokenException ex) {
            return false;
        }

        String username = jwtTokenProvider.getUsernameFromToken(refresh);
        if (!refreshTokenService.isStored(refresh, username)) {
            return false;
        }

        CustomUserDetails userDetails;
        try {
            userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            return false;
        }

        String newAccess = jwtTokenProvider.generateAccessToken(userDetails);
        String newRefresh = jwtTokenProvider.generateRefreshToken(userDetails);
        refreshTokenService.save(username, newRefresh);

        AuthController.setCookie(response, newAccess, newRefresh);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    private String extractRefreshFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                String value = cookie.getValue();
                if (StringUtils.hasText(value)) {
                    return value;
                }
            }
        }
        return null;
    }
}