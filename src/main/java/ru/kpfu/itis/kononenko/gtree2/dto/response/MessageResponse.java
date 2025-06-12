package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;

@Builder
public record MessageResponse(
        Long id,
        String sender,
        String content,
        String createdAt
) {}