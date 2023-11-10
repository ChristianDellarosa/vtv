package com.vtv.appointment.exception.commons;

import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BaseException extends RuntimeException {
    private final ExceptionError exceptionError;

    public BaseException(ExceptionError exceptionError) {
        super(exceptionError.description());
        this.exceptionError = exceptionError;
    }
    public BaseException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError.description(), cause);
        this.exceptionError = exceptionError;
    }

    @Override
    public String getMessage() {
        return this.toString();
    }
}
