package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "E-mail уже используется";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
