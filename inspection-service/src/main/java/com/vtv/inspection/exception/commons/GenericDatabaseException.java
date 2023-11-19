package com.vtv.inspection.exception.commons;

import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GenericDatabaseException extends BaseException {

    public GenericDatabaseException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
