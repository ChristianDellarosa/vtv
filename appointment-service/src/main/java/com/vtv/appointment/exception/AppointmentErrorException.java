package com.vtv.appointment.exception;

import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppointmentErrorException extends BaseException {

    public AppointmentErrorException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
