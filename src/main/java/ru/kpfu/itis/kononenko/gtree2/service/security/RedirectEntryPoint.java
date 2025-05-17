package ru.kpfu.itis.kononenko.gtree2.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@Component
public class RedirectEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse resp,
                         AuthenticationException ex) throws IOException {

        String accept = Optional.ofNullable(req.getHeader("Accept")).orElse("");

        if (accept.contains("text/html")) {             // браузер хочет страницу
            resp.sendRedirect("/auth/sign-in");
        } else {                                        // XHR, mobile, Postman …
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("""
                    {"status":"error","message":"Unauthorized"}""");
        }
    }
}
