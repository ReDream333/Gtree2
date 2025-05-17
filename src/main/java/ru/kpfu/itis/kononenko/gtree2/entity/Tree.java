package ru.kpfu.itis.kononenko.gtree2.entity;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record Tree(
        Long id,
        Long userId,
        String name,
        boolean isPrivate,
        Timestamp createdAt
) {}
