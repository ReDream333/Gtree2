package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Documented
public @interface NotBlankIfPresent {
    String message() default "Поле не может быть пустым";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
