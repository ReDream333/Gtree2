package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;

@Builder
public record CompatibilityResponse(Integer percent) {}