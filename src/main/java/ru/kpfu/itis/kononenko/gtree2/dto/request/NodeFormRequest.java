package ru.kpfu.itis.kononenko.gtree2.dto.request;// NodeForm.java
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;


@Builder
public record NodeFormRequest(
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotBlank(message = "Пол обязателен")
    Character gender,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthDate,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate deathDate,

    String comment,
    Long childId, //если null - то корень
    String photoUrl
    ){

}
