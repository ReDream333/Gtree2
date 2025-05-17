package ru.kpfu.itis.kononenko.gtree2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.kpfu.itis.kononenko.gtree2.mapper.UserMapper;
import ru.kpfu.itis.kononenko.gtree2.properties.MailProps;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties({MailProps.class})
public class GTree2Application {


    public static void main(String[] args) {
        SpringApplication.run(GTree2Application.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
