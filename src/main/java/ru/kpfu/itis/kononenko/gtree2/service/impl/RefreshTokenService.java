package ru.kpfu.itis.kononenko.gtree2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.config.property.JwtProperties;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    public void save(String login, String refreshToken) {
        redisTemplate.opsForValue().set(
                login,
                refreshToken,
                Duration.ofMillis(jwtProperties.getRefreshExpiration())
        );
    }

    public boolean isStored(String token, String login) {
        String storedToken = redisTemplate.opsForValue().get(login);
        return token.equals(storedToken);
    }

    public void invalidate(String login) {
        redisTemplate.delete(login);
    }
}