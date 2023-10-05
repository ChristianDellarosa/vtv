package com.vtv.appointment.model.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<LocalDateTimeRange, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        // Valida que la hora esté comprendida entre las 9 AM y las 18 PM y tiene que ser el anio corriente
        System.out.println(value);
        int hora = value.getHour();
        return hora >= 9 && hora <= 18 && LocalDateTime.now().getYear() == value.getYear(); // ya que es anual (Mover a otro validador)
    }
}
