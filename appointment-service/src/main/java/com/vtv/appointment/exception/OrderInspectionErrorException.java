package com.vtv.appointment.exception;

import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ExceptionError;

public class OrderInspectionErrorException extends BaseException {
    public OrderInspectionErrorException(ExceptionError exceptionError) {
        super(exceptionError);
    }

    public OrderInspectionErrorException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
