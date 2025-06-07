package ru.kpfu.itis.kononenko.gtree2.dto;// TreeForm.java
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Todo сделать рекорд
@Data
public class TreeForm {
    @NotBlank
    private String treeName;
    private boolean privateTree;
}
