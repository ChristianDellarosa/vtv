package com.vtv.appointment.model.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {

    String message() default "La fecha del turno debera ser posterior al dia actual";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
