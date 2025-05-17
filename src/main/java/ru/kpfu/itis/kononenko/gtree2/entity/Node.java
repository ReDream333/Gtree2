package ru.kpfu.itis.kononenko.gtree2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.sql.Date;

@Builder
public record Node(
        Long id,
        Long treeId,
        String firstName,
        String lastName,
        char gender,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date birthDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date deathDate,
        String comment,
        String photo
) {}
