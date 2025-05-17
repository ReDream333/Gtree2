package ru.kpfu.itis.kononenko.gtree2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailConfig {

    private String content;
    private String subject;
    private String from;
    private String sender;
    private String to;
}
