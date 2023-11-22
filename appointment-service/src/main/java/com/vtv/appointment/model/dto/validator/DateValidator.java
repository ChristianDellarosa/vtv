package com.vtv.appointment.model.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        // Valida que la fecha del turno sea posterior a la actualidad
        final var now = LocalDateTime.now();
        return value.getDayOfYear() >= LocalDateTime.now().getDayOfYear()  //TODO: Delete = for DEMO
                && value.getHour() > now.getHour(); //TODO: Delete for Demo
    }
}
