package ru.kpfu.itis.kononenko.gtree2.dto;// NodeForm.java
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Date;

@Data
public class NodeForm {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private char gender;          // 'M' | 'F'
    private Date birthDate;
    private Date deathDate;
    private String comment;
}
