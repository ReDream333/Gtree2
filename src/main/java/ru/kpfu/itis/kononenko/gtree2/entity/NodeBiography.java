package ru.kpfu.itis.kononenko.gtree2.entity;

import lombok.Builder;

@Builder
public record NodeBiography(
        Long id,
        Long nodeId,
        String biography
){}
