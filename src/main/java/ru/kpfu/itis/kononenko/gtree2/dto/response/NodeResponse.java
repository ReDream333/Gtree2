package ru.kpfu.itis.kononenko.gtree2.dto.response;


import lombok.Builder;

@Builder
public record NodeResponse(
    Long key,
    String fullName,
    String gender,
    String birthday,
    String death,
    String comment,
    String photo,
    String zodiacSign

) {
}
