package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;

@Builder
public record TreeResponse(
        Long id,
        String name,
        boolean isPrivate,
        Long userId
) {}