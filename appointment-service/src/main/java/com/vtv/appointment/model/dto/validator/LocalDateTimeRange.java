package com.vtv.appointment.model.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocalDateTimeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimeRange {

    String message() default "El tiempo debe estar comprendido entre las 9 AM y las 18 PM";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int startHour();

    int endHour();
}
