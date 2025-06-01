package ru.kpfu.itis.kononenko.gtree2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.kpfu.itis.kononenko.gtree2.validation.NotBlankIfPresent;
import ru.kpfu.itis.kononenko.gtree2.validation.UniqueEmail;
import ru.kpfu.itis.kononenko.gtree2.validation.UniqueLogin;

public record UserRequest (


        @NotBlankIfPresent
        @Size(max = 50, message = "Имя не должно превышать 50 символов")
        @UniqueLogin
        String username,

        @NotBlankIfPresent
        @NotBlank(message = "Поле email не может быть пустым")
        @UniqueEmail
        String email
) {

}
