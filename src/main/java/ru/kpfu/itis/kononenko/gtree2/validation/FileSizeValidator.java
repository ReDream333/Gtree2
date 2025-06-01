package ru.kpfu.itis.kononenko.gtree2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile[]> {
    private long maxBytes;

    @Override
    public void initialize(ValidFileSize constraintAnnotation) {
        this.maxBytes = constraintAnnotation.maxBytes();
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        if (files == null) return true;
        for (MultipartFile file : files) {
            if (file.getSize() > maxBytes) return false;
        }
        return true;
    }
}