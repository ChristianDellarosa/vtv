package com.vtv.appointment.exception;

import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppointmentAlreadyExistsException extends BaseException {
    public AppointmentAlreadyExistsException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
