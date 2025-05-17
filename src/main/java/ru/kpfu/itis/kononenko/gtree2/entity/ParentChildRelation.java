package ru.kpfu.itis.kononenko.gtree2.entity;

import lombok.Builder;

@Builder
public record ParentChildRelation(
        Long id,
        Long parentId,
        Long childId
) {}
