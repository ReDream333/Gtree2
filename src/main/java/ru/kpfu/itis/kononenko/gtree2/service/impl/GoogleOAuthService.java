package ru.kpfu.itis.kononenko.gtree2.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.config.property.GoogleOAuthProperties;
import ru.kpfu.itis.kononenko.gtree2.dto.GoogleUserInfo;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final OkHttpClient httpClient;
    private final GoogleOAuthProperties properties;
    private final ObjectMapper mapper = new ObjectMapper();

    public GoogleUserInfo fetchUser(String code) throws IOException {
        RequestBody tokenBody = new FormBody.Builder()
                .add("code", code)
                .add("client_id", properties.getClientId())
                .add("client_secret", properties.getClientSecret())
                .add("redirect_uri", properties.getRedirectUri())
                .add("grant_type", "authorization_code")
                .build();

        Request tokenRequest = new Request.Builder()
                .url("https://oauth2.googleapis.com/token")
                .post(tokenBody)
                .build();

        String accessToken;
        try (Response tokenResponse = httpClient.newCall(tokenRequest).execute()) {
            if (!tokenResponse.isSuccessful() || tokenResponse.body() == null) {
                throw new IOException("Failed to get token: " + tokenResponse);
            }
            accessToken = mapper.readTree(tokenResponse.body().string())
                    .get("access_token").asText();
        }

        Request infoRequest = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v2/userinfo")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response infoResponse = httpClient.newCall(infoRequest).execute()) {
            if (!infoResponse.isSuccessful() || infoResponse.body() == null) {
                throw new IOException("Failed to get user info");
            }
            return mapper.readValue(infoResponse.body().string(), GoogleUserInfo.class);
        }
    }
}