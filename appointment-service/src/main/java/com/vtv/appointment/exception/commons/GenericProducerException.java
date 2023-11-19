package com.vtv.appointment.exception.commons;

import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GenericProducerException extends BaseException {
    public GenericProducerException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
