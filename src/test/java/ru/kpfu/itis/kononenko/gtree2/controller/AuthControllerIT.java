package ru.kpfu.itis.kononenko.gtree2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.kpfu.itis.kononenko.gtree2.dto.request.UserLoginRequest;
import ru.kpfu.itis.kononenko.gtree2.enums.TokenStatus;
import ru.kpfu.itis.kononenko.gtree2.service.*;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtService;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(AuthController.class)
class AuthControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean AuthenticationManager authManager;
    @MockitoBean JwtService jwt;
    @MockitoBean UserService userService;
    @MockitoBean VerificationTokenService tokenService;
    @MockitoBean MailService mailService;

    @Test
    void loginSuccess() throws Exception {
        Authentication fakeAuth =
                new UsernamePasswordAuthenticationToken("alice", null);
        given(authManager.authenticate(any(Authentication.class)))
                .willReturn(fakeAuth);
        given(jwt.generate(any())).willReturn("FAKE-TOKEN");

        mvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new UserLoginRequest("alice", "pass"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value("FAKE-TOKEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirectUrl")
                        .value("/profile"));
    }

    @Test
    void confirmValid() throws Exception {
        given(tokenService.verifyToken("XYZ")).willReturn(TokenStatus.VALID);

        mvc.perform(MockMvcRequestBuilders.get("/auth/confirm")
                        .param("token", "XYZ"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(org.hamcrest.Matchers.containsString(
                                "Аккаунт активирован")));
    }

    @Test
    void profileSecured() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        mvc.perform(MockMvcRequestBuilders.get("/profile")
                        .with(user("alice")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile"));
    }
}
