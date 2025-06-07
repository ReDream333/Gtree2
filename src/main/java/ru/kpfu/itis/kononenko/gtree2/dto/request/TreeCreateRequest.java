package ru.kpfu.itis.kononenko.gtree2.dto.request;

import lombok.Builder;

@Builder
public record TreeCreateRequest(
        Long userId,
        String name,
        boolean isPrivate
) {
}
