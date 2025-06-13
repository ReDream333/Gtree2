package ru.kpfu.itis.kononenko.gtree2.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth.google")
@Data
public class GoogleOAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}