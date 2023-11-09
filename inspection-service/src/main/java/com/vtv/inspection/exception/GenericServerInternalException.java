package com.vtv.inspection.exception;

import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GenericServerInternalException extends BaseException {
    public GenericServerInternalException(ExceptionError exceptionError) {
        super(exceptionError);
    }

    public GenericServerInternalException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
