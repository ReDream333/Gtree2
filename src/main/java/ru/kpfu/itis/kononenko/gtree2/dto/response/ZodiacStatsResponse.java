package ru.kpfu.itis.kononenko.gtree2.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record ZodiacStatsResponse(
        Map<String, Long> stats,
        String message
) {}