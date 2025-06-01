package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLoginValidator.class)
public @interface UniqueLogin {
    String message() default "Логин уже занят";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
