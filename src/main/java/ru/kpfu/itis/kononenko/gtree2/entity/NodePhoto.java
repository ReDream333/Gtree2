package ru.kpfu.itis.kononenko.gtree2.entity;

import lombok.Builder;

@Builder
public record NodePhoto(
        Long id,
        Long nodeId,
        String photoUrl,
        String description
){}
