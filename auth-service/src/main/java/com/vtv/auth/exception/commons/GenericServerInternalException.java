package com.vtv.auth.exception.commons;

import com.vtv.auth.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GenericServerInternalException extends BaseException {
    public GenericServerInternalException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
