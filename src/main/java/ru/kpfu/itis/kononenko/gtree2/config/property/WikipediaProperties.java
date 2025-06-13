package ru.kpfu.itis.kononenko.gtree2.config.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wikipedia.api")
@Data
public class WikipediaProperties {
    private String url ;
}