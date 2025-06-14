package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record UserResponse(
        String username,
        String email,
        Timestamp createdAt,
        Boolean emailVerified,
        String photo
){}