package com.vtv.inspection.exception;

import com.vtv.inspection.exception.commons.BaseException;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UnauthorizedUserException extends BaseException {
    public UnauthorizedUserException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
