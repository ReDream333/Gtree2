package ru.kpfu.itis.kononenko.gtree2.dto.request;

public record TreeCreateRequest(
        Long userId,
        String name,
        boolean isPrivate
) {
}
