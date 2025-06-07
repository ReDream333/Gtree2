package ru.kpfu.itis.kononenko.gtree2.dto.response;

public record RelationResponse(
    Long from,  // childId
    Long to     // parentId
) {}