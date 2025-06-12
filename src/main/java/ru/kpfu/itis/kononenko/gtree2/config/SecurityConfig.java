package ru.kpfu.itis.kononenko.gtree2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetailsService;
import ru.kpfu.itis.kononenko.gtree2.service.security.JwtFilter;
import ru.kpfu.itis.kononenko.gtree2.service.security.RedirectEntryPoint;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final RedirectEntryPoint redirectEntryPoint;
    private final String AUTH_ENTRY_POINTS = "/auth/**";
    private final String SING_IN_ENTRY_POINT = "/auth/sign-in";
    private final String SING_UP_ENTRY_POINT = "/auth/sign-up";
    private final String INDEX_ENTRY_POINT = "/";
    private final String PROFILE_ENTRY_POINT = "/profile/**";
    private final String USER_API_ENTRY_POINT = "/users/api/**";

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error/**"
                        )
                        .permitAll()
                        .requestMatchers(
                                AUTH_ENTRY_POINTS,
                                INDEX_ENTRY_POINT,
                                SING_UP_ENTRY_POINT,
                                USER_API_ENTRY_POINT,
                                PROFILE_ENTRY_POINT
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                              redirectEntryPoint)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl(INDEX_ENTRY_POINT).permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }


}
