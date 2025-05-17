package ru.kpfu.itis.kononenko.gtree2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserLoginRequest(
        @NotBlank(message = "Поле username не может быть пустым")
        String username,

        @NotBlank(message = "Поле password не может быть пустым")
        @Size(min = 6, message = "Пароль должен быть более 6 символов")
        String password
) {
}
