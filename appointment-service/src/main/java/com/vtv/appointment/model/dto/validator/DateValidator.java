package com.vtv.appointment.model.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        // Valida que la fecha del turno sea posterior a la actualidad
        return value.getDayOfYear() > LocalDateTime.now().getDayOfYear();
    }
}
