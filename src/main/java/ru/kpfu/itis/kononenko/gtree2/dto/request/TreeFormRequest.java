package ru.kpfu.itis.kononenko.gtree2.dto.request;// TreeForm.java
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

//Todo сделать рекорд
@Builder
public record TreeFormRequest(
        Long userId,
        @NotBlank
        String treeName,
        boolean privateTree
) {

}
