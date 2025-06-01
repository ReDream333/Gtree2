package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileSize {
    String message() default "Размер файла превышает допустимый лимит";
    long maxBytes();
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}