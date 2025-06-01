package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator
        implements ConstraintValidator<NotBlankIfPresent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext c) {
        return value == null || !value.trim().isEmpty();   // null допустим, ""—нельзя
    }
}
