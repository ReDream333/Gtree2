package ru.kpfu.itis.kononenko.gtree2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.kononenko.gtree2.config.property.MailProps;


@SpringBootApplication
@EnableConfigurationProperties({MailProps.class})
@OpenAPIDefinition(info = @Info(title = "GTree2 API", version = "v1"))
public class GTree2Application {


    public static void main(String[] args) {
        SpringApplication.run(GTree2Application.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
