package ru.kpfu.itis.kononenko.gtree2.dto;


import lombok.Builder;

@Builder
public record NodeDto(
    Long id,
    String firstName,
    String lastName,
    char gender,
    String birthDate,
    String deathDate,
    String comment
) {
}
