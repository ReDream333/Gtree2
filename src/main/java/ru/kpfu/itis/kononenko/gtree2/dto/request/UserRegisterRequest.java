package ru.kpfu.itis.kononenko.gtree2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.kpfu.itis.kononenko.gtree2.validation.UniqueEmail;
import ru.kpfu.itis.kononenko.gtree2.validation.UniqueLogin;

@Builder

public record UserRegisterRequest(
        @NotBlank(message = "Поле username не может быть пустым")
        @Size(max = 50, message = "Имя не должно превышать 50 символов")
        @UniqueLogin
        String username,

        @Email(message = "Почта должна быть валидной")
        @NotBlank(message = "Поле email не может быть пустым")
        @UniqueEmail
        String email,

        @NotBlank(message = "Поле password не может быть пустым")
        @Size(min = 6, message = "Пароль должен быть более 6 символов")
        @Size(max = 100, message = "Пароль не должен превышать 100 символов")
        String password
) { }
